/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.core;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import com._1c.g5.v8.dt.xml.LineFeedConverter;

import ru.capralow.dt.modeling.core.IExportArtifactBuilder;

public final class UnsupportedFilesCopyVisitor
    extends SimpleFileVisitor<Path>
{
    private final Path sourceRoot;
    private final IExportArtifactBuilder artifactBuilder;
    private final Map<Predicate<Path>, LineFeedConverter.ConvertOption[]> modificators;
    private final Predicate<Path> exclusion;

    private UnsupportedFilesCopyVisitor(final Builder builder)
    {
        this.sourceRoot = builder.sourceRoot;
        this.artifactBuilder = builder.artifactBuilder;
        this.modificators = builder.modificators;
        this.exclusion = builder.exclusion;
    }

    @Override
    public FileVisitResult visitFile(final Path sourcePath, final BasicFileAttributes attrs) throws IOException
    {
        if (this.exclusion == null || !this.exclusion.test(sourcePath))
        {
            final Path targetPath = this.sourceRoot.relativize(sourcePath);
            final Optional<LineFeedConverter.ConvertOption[]> modificator = this.modificators.entrySet()
                .parallelStream()
                .filter(e -> e.getKey().test(sourcePath))
                .findAny()
                .map(Map.Entry::getValue);
            if (modificator.isPresent())
            {
                IOException t = null;
                try
                {
                    final InputStream inputStream =
                        new BufferedInputStream(Files.newInputStream(sourcePath, new OpenOption[0]));
                    try
                    {
                        final OutputStream outputStream = this.artifactBuilder.newOutputStream(targetPath);
                        try
                        {
                            LineFeedConverter.convert(inputStream, outputStream, modificator.get());
                        }
                        finally
                        {
                            if (outputStream != null)
                            {
                                outputStream.close();
                            }
                        }
                        if (inputStream != null)
                        {
                            inputStream.close();
                            return super.visitFile(sourcePath, attrs);
                        }
                        return super.visitFile(sourcePath, attrs);
                    }
                    catch (IOException exception)
                    {
                        if (t == null)
                        {
                            t = exception;
                        }
                        else
                        {
                            if (t != exception)
                            {
                                t.addSuppressed(exception);
                            }
                        }
                        if (inputStream != null)
                        {
                            inputStream.close();
                        }
                    }
                }
                catch (IOException exception2)
                {
                    if (t == null)
                    {
                        t = exception2;
                    }
                    else
                    {
                        if (t != exception2)
                        {
                            t.addSuppressed(exception2);
                        }
                    }
                }

                if (t != null)
                {
                    throw t;
                }
            }

            this.artifactBuilder.copy(sourcePath, targetPath);
        }
        return super.visitFile(sourcePath, attrs);
    }

    public static class Builder
    {
        private final Path sourceRoot;
        private final IExportArtifactBuilder artifactBuilder;
        private Map<Predicate<Path>, LineFeedConverter.ConvertOption[]> modificators;
        private Predicate<Path> exclusion;

        public Builder(final Path sourceRoot, final IExportArtifactBuilder artifactBuilder)
        {
            this.modificators = new HashMap<>();
            this.sourceRoot = sourceRoot;
            this.artifactBuilder = artifactBuilder;
        }

        public Builder addExclusion(final Predicate<Path> exclusion)
        {
            this.exclusion = exclusion;
            return this;
        }

        public Builder putModifier(final Predicate<Path> pathPredicate,
            final LineFeedConverter.ConvertOption... convertOptions)
        {
            this.modificators.put(pathPredicate, convertOptions);
            return this;
        }

        public UnsupportedFilesCopyVisitor build()
        {
            return new UnsupportedFilesCopyVisitor(this);
        }
    }

    public static class FileExtensionPredicateBuilder
    {
        public static Predicate<Path> build(final String... extensions)
        {
            return path -> {
                final String fileName = path.getFileName().toString();
                final int length = extensions.length;
                int i = 0;
                while (i < length)
                {
                    String extension = extensions[i];
                    if (fileName.endsWith(extension))
                    {
                        return true;
                    }
                    else
                    {
                        ++i;
                    }
                }
                return false;
            };
        }
    }
}
