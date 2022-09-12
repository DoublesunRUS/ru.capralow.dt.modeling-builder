/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.core;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

public interface IExportArtifactBuilder
    extends Closeable
{
    OutputStream newOutputStream(Path p0) throws IOException;

    void copy(Path p0, Path p1) throws IOException;
}
