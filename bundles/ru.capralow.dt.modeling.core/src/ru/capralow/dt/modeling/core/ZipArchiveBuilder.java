/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.core;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.runtime.Assert;

public final class ZipArchiveBuilder
    implements IExportArtifactBuilder
{
    public static ZipArchiveBuilder create(final OutputStream outputStream, final int level,
        final long maxQueuedDataSize) throws IOException
    {
        Assert.isLegal(outputStream != null, "Argument 'outputStream' may not be null");
        Assert.isLegal(level >= 0 && level <= 9, "Argument 'level' must be between 0 and 9");
        Assert.isLegal(maxQueuedDataSize > 0L, "Argument 'maxQueuedDataSize' must be greater than 0");
        final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        zipOutputStream.setLevel(level);
        return new ZipArchiveBuilder(zipOutputStream, maxQueuedDataSize, false);
    }

    public static ZipArchiveBuilder create(final Path path, final int level, final long maxQueuedDataSize)
        throws IOException
    {
        Assert.isLegal(path != null, "Argument 'path' may not be null");
        Assert.isLegal(level >= 0 && level <= 9, "Argument 'level' must be between 0 and 9");
        Assert.isLegal(maxQueuedDataSize > 0L, "Argument 'maxQueuedDataSize' must be greater than 0");
        Files.createDirectories(path.getParent(), new FileAttribute[0]);
        final ZipOutputStream zipOutputStream =
            new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(path, new OpenOption[0])));
        zipOutputStream.setLevel(level);
        return new ZipArchiveBuilder(zipOutputStream, maxQueuedDataSize, true);
    }

    private static String toString(final Path path)
    {
        if (path.getNameCount() == 1)
        {
            return path.toString();
        }
        final StringBuilder result = new StringBuilder();
        final Iterator<Path> iterator = path.iterator();
        while (iterator.hasNext())
        {
            result.append(iterator.next().toString());
            if (iterator.hasNext())
            {
                result.append('/');
            }
        }
        return result.toString();
    }

    private final ZipOutputStream zipOutputStream;
    private final long maxQueuedDataSize;
    private final boolean closeStream;
    private final ReentrantLock writeZipLock;
    private final Object writeRequestQueueLock;
    private Queue<WriteRequest> writeRequestQueue;
    private Set<String> writtenPaths;

    private long queuedDataSize;

    private volatile boolean broken;

    private volatile boolean closed;

    private ZipArchiveBuilder(final ZipOutputStream zipOutputStream, final long maxQueuedDataSize,
        final boolean closeStream)
    {
        this.zipOutputStream = zipOutputStream;
        this.maxQueuedDataSize = maxQueuedDataSize;
        this.closeStream = closeStream;
        this.writeRequestQueue = new LinkedList<>();
        this.queuedDataSize = 0L;
        this.writtenPaths = new HashSet<>();
        this.writeZipLock = new ReentrantLock();
        this.writeRequestQueueLock = new Object();
    }

    @Override
    public void close() throws IOException
    {
        this.ensureOpen();
        this.closed = true;
        this.writeZipLock.lock();
        try
        {
            try
            {
                if (!this.broken)
                {
                    this.finishWriteZip();
                }
            }
            finally
            {
                if (this.closeStream)
                {
                    this.zipOutputStream.close();
                }
                else if (!this.broken)
                {
                    this.zipOutputStream.finish();
                }
            }
            if (this.closeStream)
            {
                this.zipOutputStream.close();
            }
            else if (!this.broken)
            {
                this.zipOutputStream.finish();
            }
        }
        finally
        {
            this.writeZipLock.unlock();
        }
        this.writeZipLock.unlock();
    }

    @Override
    public void copy(final Path sourcePath, final Path targetPath) throws IOException
    {
        Assert.isLegal(sourcePath != null, "Argument 'sourcePath' may not be null");
        Assert.isLegal(targetPath != null, "Argument 'targetPath' may not be null");
        Assert.isLegal(!targetPath.isAbsolute(), "Argument 'targetPath' must be relative");
        this.ensureOpen();
        this.ensureNotBroken();
        if (Files.isDirectory(sourcePath, new LinkOption[0]))
        {
            Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException
                {
                    ZipArchiveBuilder.this.addCopyRequest(targetPath.resolve(sourcePath.relativize(file)), file);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        else
        {
            this.addCopyRequest(targetPath, sourcePath);
        }
    }

    @Override
    public OutputStream newOutputStream(final Path path) throws IOException
    {
        Assert.isLegal(path != null, "Argument 'path' may not be null");
        Assert.isLegal(!path.isAbsolute(), "Argument 'path' must be relative");
        this.ensureOpen();
        this.ensureNotBroken();
        return new ByteArrayOutputStream()
        {
            @Override
            public void close() throws IOException
            {
                ZipArchiveBuilder.this.ensureOpen();
                ZipArchiveBuilder.this.ensureNotBroken();
                ZipArchiveBuilder.this.addWriteRequest(path, this.toByteArray());
            }
        };
    }

    private void addCopyRequest(final Path targetPath, final Path sourcePath) throws IOException
    {
        this.addWriteRequest(targetPath, Files.readAllBytes(sourcePath));
    }

    private void addWriteRequest(final Path path, final byte[] bytes) throws IOException
    {
        final WriteRequest writeRequest = new WriteRequest();
        writeRequest.path = toString(path);
        writeRequest.bytes = bytes;
        if (this.enqueueWriteRequest(writeRequest))
        {
            this.writeZip();
        }
    }

    private boolean enqueueWriteRequest(final WriteRequest writeRequest) throws IOException
    {
        synchronized (this.writeRequestQueueLock)
        {
            if (!this.writtenPaths.add(writeRequest.path))
            {
                // monitorexit(this.writeRequestQueueLock)
                return false;
            }
            while (this.queuedDataSize >= this.maxQueuedDataSize)
            {
                this.ensureNotBroken();
                try
                {
                    this.writeRequestQueueLock.wait();
                }
                catch (InterruptedException e)
                {
                    throw new IOException("Received interruption signal", e);
                }
            }
            this.writeRequestQueue.add(writeRequest);
            this.queuedDataSize += writeRequest.bytes.length;
        }
        // monitorexit(this.writeRequestQueueLock)
        return true;
    }

    private void ensureNotBroken()
    {
        if (this.broken)
        {
            throw new IllegalStateException("Builder is broken");
        }
    }

    private void ensureOpen()
    {
        if (this.closed)
        {
            throw new IllegalStateException("Builder is closed");
        }
    }

    private void finishWriteZip() throws IOException
    {
        while (true)
        {
            final WriteRequest writeRequest;
            synchronized (this.writeRequestQueueLock)
            {
                writeRequest = this.writeRequestQueue.poll();
                if (writeRequest == null)
                {
                    // monitorexit(this.writeRequestQueueLock)
                    break;
                }
            }
            // monitorexit(this.writeRequestQueueLock)
            this.processSingleWriteRequest(writeRequest);
        }
    }

    private void processSingleWriteRequest(final WriteRequest writeRequest) throws IOException
    {
        this.zipOutputStream.putNextEntry(new ZipEntry(writeRequest.path));
        this.zipOutputStream.write(writeRequest.bytes, 0, writeRequest.bytes.length);
        this.zipOutputStream.closeEntry();
        writeRequest.path = null;
        writeRequest.bytes = null;
    }

    private void writeZip() throws IOException
    {
        if (this.writeZipLock.tryLock())
        {
            boolean completedSuccessfully = false;
            try
            {
                while (true)
                {
                    final WriteRequest writeRequest;
                    synchronized (this.writeRequestQueueLock)
                    {
                        writeRequest = this.writeRequestQueue.poll();
                        if (writeRequest == null)
                        {
                            this.writeZipLock.unlock();
                            completedSuccessfully = true;
                            // monitorexit(this.writeRequestQueueLock)
                            break;
                        }
                        final long oldQueuedDataSize = this.queuedDataSize;
                        this.queuedDataSize = oldQueuedDataSize - writeRequest.bytes.length;
                        if (oldQueuedDataSize >= this.maxQueuedDataSize && this.queuedDataSize < this.maxQueuedDataSize)
                        {
                            this.writeRequestQueueLock.notifyAll();
                        }
                    }
                    // monitorexit(this.writeRequestQueueLock)
                    this.processSingleWriteRequest(writeRequest);
                }
            }
            finally
            {
                if (!completedSuccessfully)
                {
                    synchronized (this.writeRequestQueueLock)
                    {
                        this.broken = true;
                        this.writeRequestQueue.clear();
                        this.writeRequestQueueLock.notifyAll();
                    }
                    // monitorexit(this.writeRequestQueueLock)
                    this.writeZipLock.unlock();
                }
            }
            if (!completedSuccessfully)
            {
                synchronized (this.writeRequestQueueLock)
                {
                    this.broken = true;
                    this.writeRequestQueue.clear();
                    this.writeRequestQueueLock.notifyAll();
                }
                // monitorexit(this.writeRequestQueueLock)
                this.writeZipLock.unlock();
            }
        }
    }

    boolean isWriteInProgress()
    {
        return this.writeZipLock.isLocked();
    }

    private static class WriteRequest
    {
        String path;
        byte[] bytes;
    }
}
