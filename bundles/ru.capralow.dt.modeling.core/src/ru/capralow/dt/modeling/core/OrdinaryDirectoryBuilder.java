/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.core;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;

import org.eclipse.core.runtime.Assert;

import com._1c.g5.v8.dt.common.FileUtil;

public final class OrdinaryDirectoryBuilder
    implements IExportArtifactBuilder
{
    private final Path directoryPath;
    private volatile boolean closed;

    public static OrdinaryDirectoryBuilder create(final Path directoryPath)
    {
        Assert.isLegal(directoryPath != null, "Argument 'directoryPath' may not be null");
        return new OrdinaryDirectoryBuilder(directoryPath);
    }

    private OrdinaryDirectoryBuilder(final Path directoryPath)
    {
        this.directoryPath = directoryPath;
    }

    @Override
    public OutputStream newOutputStream(final Path path) throws IOException
    {
        Assert.isLegal(path != null, "Argument 'path' may not be null");
        Assert.isLegal(!path.isAbsolute(), "Argument 'path' must be relative");
        this.ensureOpen();
        final Path fullPath = this.directoryPath.resolve(path);
        Files.createDirectories(fullPath.getParent(), new FileAttribute[0]);
        return new BufferedOutputStream(Files.newOutputStream(fullPath, new OpenOption[0]));
    }

    @Override
    public void copy(final Path sourcePath, final Path targetPath) throws IOException
    {
        Assert.isLegal(sourcePath != null, "Argument 'sourcePath' may not be null");
        Assert.isLegal(targetPath != null, "Argument 'targetPath' may not be null");
        Assert.isLegal(!targetPath.isAbsolute(), "Argument 'targetPath' must be relative");
        this.ensureOpen();
        final Path targetFullPath = this.directoryPath.resolve(targetPath);
        if (Files.isDirectory(sourcePath, new LinkOption[0]))
        {
            Files.createDirectories(targetFullPath, new FileAttribute[0]);
            FileUtil.copyRecursively(sourcePath, targetFullPath);
        }
        else
        {
            Files.createDirectories(targetFullPath.getParent(), new FileAttribute[0]);
            Files.copy(sourcePath, targetFullPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Override
    public void close() throws IOException
    {
        this.ensureOpen();
        this.closed = true;
    }

    private void ensureOpen()
    {
        if (this.closed)
        {
            throw new IllegalStateException("Builder is closed");
        }
    }
}
