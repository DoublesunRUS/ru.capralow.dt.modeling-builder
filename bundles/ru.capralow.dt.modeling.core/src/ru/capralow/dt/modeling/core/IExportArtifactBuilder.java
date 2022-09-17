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
    void copy(Path paramPath1, Path paramPath2) throws IOException;

    OutputStream newOutputStream(Path paramPath) throws IOException;
}
