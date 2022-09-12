/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.core;

import java.io.IOException;

public interface IExportArtifactBuilderFactory
{
    IExportArtifactBuilder createArtifactBuilder() throws IOException;
}
