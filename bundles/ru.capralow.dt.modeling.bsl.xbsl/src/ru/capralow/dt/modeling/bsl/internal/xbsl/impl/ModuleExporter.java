/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.bsl.internal.xbsl.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.util.Strings;

import com._1c.g5.v8.dt.bsl.model.BinaryExpression;
import com._1c.g5.v8.dt.bsl.model.BooleanLiteral;
import com._1c.g5.v8.dt.bsl.model.Conditional;
import com._1c.g5.v8.dt.bsl.model.DynamicFeatureAccess;
import com._1c.g5.v8.dt.bsl.model.EmptyStatement;
import com._1c.g5.v8.dt.bsl.model.ExecuteStatement;
import com._1c.g5.v8.dt.bsl.model.Expression;
import com._1c.g5.v8.dt.bsl.model.FeatureAccess;
import com._1c.g5.v8.dt.bsl.model.ForEachStatement;
import com._1c.g5.v8.dt.bsl.model.ForToStatement;
import com._1c.g5.v8.dt.bsl.model.FormalParam;
import com._1c.g5.v8.dt.bsl.model.IfStatement;
import com._1c.g5.v8.dt.bsl.model.IndexAccess;
import com._1c.g5.v8.dt.bsl.model.Invocation;
import com._1c.g5.v8.dt.bsl.model.Method;
import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.model.NumberLiteral;
import com._1c.g5.v8.dt.bsl.model.OperatorStyleCreator;
import com._1c.g5.v8.dt.bsl.model.RaiseStatement;
import com._1c.g5.v8.dt.bsl.model.ReturnStatement;
import com._1c.g5.v8.dt.bsl.model.SimpleStatement;
import com._1c.g5.v8.dt.bsl.model.Statement;
import com._1c.g5.v8.dt.bsl.model.StaticFeatureAccess;
import com._1c.g5.v8.dt.bsl.model.StringLiteral;
import com._1c.g5.v8.dt.bsl.model.TryExceptStatement;
import com._1c.g5.v8.dt.bsl.model.UnaryExpression;
import com._1c.g5.v8.dt.bsl.model.UndefinedLiteral;
import com._1c.g5.v8.dt.bsl.model.WhileStatement;
import com._1c.g5.v8.dt.bsl.resource.TypesComputer;
import com._1c.g5.v8.dt.bsl.resource.extension.IBslResourceExtension;
import com._1c.g5.v8.dt.bsl.resource.extension.IBslResourceExtensionManager;
import com._1c.g5.v8.dt.bsl.util.BslUtil;
import com._1c.g5.v8.dt.common.FileUtil;
import com._1c.g5.v8.dt.core.filesystem.IProjectFileSystemSupport;
import com._1c.g5.v8.dt.core.filesystem.IProjectFileSystemSupportProvider;
import com._1c.g5.v8.dt.core.platform.IConfigurationAware;
import com._1c.g5.v8.dt.core.platform.IResourceLookup;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;
import com._1c.g5.v8.dt.mcore.Type;
import com._1c.g5.v8.dt.mcore.TypeItem;
import com._1c.g5.v8.dt.metadata.mdclass.Configuration;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.bsl.internal.xbsl.BslXBslPlugin;
import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.core.IExportArtifactBuilder;
import ru.capralow.dt.modeling.yaml.BasicExporter;
import ru.capralow.dt.modeling.yaml.IExportContext;
import ru.capralow.dt.modeling.yaml.IExporter;

@Singleton
public class ModuleExporter
    extends BasicExporter
{
    private static final String DOT_XBSL_EXT = ".xbsl"; //$NON-NLS-1$
    private static final String DOT_BIN_EXT = ".bin"; //$NON-NLS-1$

    public static String getModuleFileExtension(Module module) throws IOException
    {
        URI uri = EcoreUtil.getURI(Preconditions.checkNotNull(module)).trimFragment();

        String result;

        try (InputStream bslInputStream = URIConverter.INSTANCE.createInputStream(uri);
            BufferedInputStream inputStream = new BufferedInputStream(bslInputStream);)
        {
            result = BslUtil.isBinaryBslFile(inputStream, (String)null) ? DOT_BIN_EXT : DOT_XBSL_EXT;
        }
        catch (IOException e)
        {
            throw e;
        }

        return result;
    }

    private final TypesComputer typesComputer;

    @Inject
    private IResourceLookup resourceLookup;

    @Inject
    private IProjectFileSystemSupportProvider fileSystemSupportProvider;

    @Inject
    private IBslResourceExtensionManager bslResourceExtensionManager;

    @Inject
    private IV8ProjectManager v8ProjectManager;

    @Inject
    public ModuleExporter(TypesComputer typesComputer)
    {
        this.typesComputer = typesComputer;
    }

    @Override
    public boolean isAppropriate(Version version, EObject eObject)
    {
        return eObject instanceof Module && super.isAppropriate(version, eObject);
    }

    @Override
    public IStatus work(EObject eObject, IExportContext exportContext, IExportArtifactBuilder artifactBuilder,
        IProgressMonitor progressMonitor)
    {
        IProject project = resourceLookup.getProject(eObject);

        IProjectFileSystemSupport fileSystemSupport = fileSystemSupportProvider.getProjectFileSystemSupport(project);

        IFile bslFile = fileSystemSupport.getFile(eObject);

        debugTrace.trace(IExporter.EXPORTER_TRACE_OPTION, MessageFormat.format("Exporting module: {0}", bslFile));

        if (bslFile == null || !bslFile.exists())
        {
            return Status.OK_STATUS;
        }

        Path targetPath = null;

        try (BufferedInputStream inputStream = new BufferedInputStream(bslFile.getContents());)
        {
            boolean isBinaryFile = BslUtil.isBinaryBslFile(inputStream, (String)null);

            Path outputPath =
                getOutputPath(eObject, isBinaryFile ? DOT_BIN_EXT : DOT_XBSL_EXT, exportContext.getProjectVersion());

            if (outputPath != null)
            {
                progressMonitor
                    .setTaskName(MessageFormat.format(Messages.ModuleExporter_Export_file__0, outputPath.toString()));

                BufferedInputStream actualStream;
                if (isBinaryFile)
                {
                    targetPath = outputPath.resolveSibling(FileUtil.trimExtension(outputPath) + DOT_BIN_EXT);

                    debugTrace.trace(IExporter.EXPORTER_TRACE_OPTION,
                        MessageFormat.format("Exporting binary module to: {0}", targetPath));

                    try (OutputStream outputStream = artifactBuilder.newOutputStream(targetPath);)
                    {
                        ByteStreams.copy(inputStream, outputStream);
                    }
                    catch (IOException e)
                    {
                        throw e;
                    }
                }
                else
                {
                    debugTrace.trace(IExporter.EXPORTER_TRACE_OPTION,
                        MessageFormat.format("Exporting module to: {0}", outputPath));

                    IBslResourceExtension resourceExtension = bslResourceExtensionManager.getResourceExtension();

                    actualStream = inputStream;
                    if (resourceExtension != null)
                    {
                        debugTrace.trace(IExporter.EXPORTER_TRACE_OPTION, MessageFormat
                            .format("Using bsl resource extension: {0}", resourceExtension.getClass().getName()));

                        InputStream replacedStream =
                            resourceExtension.replaceBslStreamIfNecessary(bslFile, inputStream);

                        if (!(replacedStream instanceof BufferedInputStream))
                        {
                            actualStream = new BufferedInputStream(replacedStream);
                        }
                        else
                        {
                            actualStream = (BufferedInputStream)replacedStream;
                        }

                        debugTrace.trace(IExporter.EXPORTER_TRACE_OPTION,
                            MessageFormat.format("Bsl stream replaced: {0}", actualStream != inputStream));
                    }

                    try (OutputStream outputStream = artifactBuilder.newOutputStream(outputPath);)
                    {
                        if (eObject instanceof Module)
                        {
                            Configuration configuration =
                                ((IConfigurationAware)v8ProjectManager.getProject(eObject)).getConfiguration();
                            Module module = (Module)EcoreUtil.resolve(eObject, configuration);

                            EList<Method> methods = module.allMethods();
                            for (Method method : methods)
                            {
                                String methodText = ""; //$NON-NLS-1$

                                if (method.isExport())
                                {
                                    methodText += "@проект" + Strings.newLine(); //$NON-NLS-1$
                                }

                                methodText += "метод " + method.getName(); //$NON-NLS-1$
                                methodText += "("; //$NON-NLS-1$

                                List<String> usedVariables = new ArrayList<>();

                                boolean firstParam = true;
                                EList<FormalParam> formalParams = method.getFormalParams();
                                for (FormalParam formalParam : formalParams)
                                {
                                    formalParam = (FormalParam)EcoreUtil.resolve(formalParam, configuration);

                                    if (!firstParam)
                                    {
                                        methodText += ", "; //$NON-NLS-1$
                                    }
                                    methodText += formalParam.getName() + ":"; //$NON-NLS-1$
                                    usedVariables.add(formalParam.getName());
                                    firstParam = false;

                                    boolean firstType = true;
                                    List<TypeItem> paramTypes =
                                        typesComputer.computeTypes(formalParam, method.environments());
                                    if (paramTypes.isEmpty())
                                    {
                                        methodText += "любой"; //$NON-NLS-1$
                                    }
                                    for (TypeItem paramType : paramTypes)
                                    {
                                        if (!firstType)
                                        {
                                            methodText += "|"; //$NON-NLS-1$
                                        }
                                        methodText += paramType.getNameRu();
                                        firstType = false;
                                    }
                                }

                                methodText += ")" + Strings.newLine(); //$NON-NLS-1$

                                EList<Statement> statements = method.allStatements();
                                for (Statement statement : statements)
                                {
                                    String statementText =
                                        ReturnStringFromStatement(statement, usedVariables, configuration);

                                    methodText += "\t" + statementText + Strings.newLine(); //$NON-NLS-1$
                                }

                                methodText += ";" + Strings.newLine() + Strings.newLine(); //$NON-NLS-1$

                                outputStream.write(methodText.getBytes());
                                outputStream.flush();
                            }

                        }
                        else
                        {
                            actualStream.transferTo(outputStream);
                        }

//                        LineFeedConverter.convert(actualStream, outputStream,
//                            new ConvertOption[] { ConvertOption.TO_CRLF, ConvertOption.WRITE_BOM });
                    }
                    catch (IOException e)
                    {
                        throw e;
                    }
                }
            }

            BslXBslPlugin.createErrorStatus(Messages.ModuleExporter_Cant_get_file_name, (Throwable)null);

            debugTrace.trace(IExporter.EXPORTER_TRACE_OPTION, "Module exported");
        }
        catch (IOException | ExportException |

            CoreException e)
        {
            debugTrace
                .trace(IExporter.EXPORTER_TRACE_OPTION,
                    MessageFormat.format("Export write to file {0} error in {1}",
                        targetPath != null ? targetPath.getFileName() : bslFile.getName(), getClass().getSimpleName()),
                    e);
            return BslXBslPlugin.createErrorStatus(e.getMessage(), e);
        }

        return Status.OK_STATUS;
    }

    protected String ReturnStringFromBinaryExpression(BinaryExpression expression, Configuration configuration)
    {
        String operation = ""; //$NON-NLS-1$

        switch (expression.getOperation().getLiteral())
        {
        case "AND": //$NON-NLS-1$
            operation = " и "; //$NON-NLS-1$
            break;
        case "EQ": //$NON-NLS-1$
            operation = " == "; //$NON-NLS-1$
            break;
        case "GT": //$NON-NLS-1$
            operation = " > "; //$NON-NLS-1$
            break;
        case "MINUS": //$NON-NLS-1$
            operation = " - "; //$NON-NLS-1$
            break;
        case "MULTIPLY": //$NON-NLS-1$
            operation = " * "; //$NON-NLS-1$
            break;
        case "NE": //$NON-NLS-1$
            operation = " != "; //$NON-NLS-1$
            break;
        case "OR": //$NON-NLS-1$
            operation = " или "; //$NON-NLS-1$
            break;
        case "PLUS": //$NON-NLS-1$
            operation = " + "; //$NON-NLS-1$
            break;
        case "LT": //$NON-NLS-1$
            operation = " < "; //$NON-NLS-1$
            break;
        default:
            operation = " "; //$NON-NLS-1$
        }

        String result = ReturnStringFromExpression(expression.getLeft(), configuration) + operation
            + ReturnStringFromExpression(expression.getRight(), configuration);

        return result;
    }

    protected String ReturnStringFromExpression(Expression expression, Configuration configuration)
    {
        if (expression instanceof StaticFeatureAccess)
        {
            return ((StaticFeatureAccess)expression).getName();
        }
        else if (expression instanceof Invocation)
        {
            Invocation invocation = (Invocation)EcoreUtil.resolve(expression, configuration);
            FeatureAccess methodAccess = invocation.getMethodAccess();

            String result = ReturnStringFromFeatureAccess(methodAccess, configuration);

            result += "("; //$NON-NLS-1$
            boolean firstParam = true;
            for (Expression param : invocation.getParams())
            {
                if (!firstParam)
                {
                    result += ", "; //$NON-NLS-1$
                }
                result += ReturnStringFromExpression(param, configuration);
                firstParam = false;
            }
            result += ")"; //$NON-NLS-1$

            return result;
        }
        else if (expression instanceof DynamicFeatureAccess)
        {
            return ReturnStringFromFeatureAccess((DynamicFeatureAccess)expression, configuration);
        }
        else if (expression instanceof OperatorStyleCreator)
        {
            Type rightType = ((OperatorStyleCreator)expression).getType();
            rightType = (Type)EcoreUtil.resolve(rightType, configuration);

            String result = "новый "; //$NON-NLS-1$

            switch (rightType.getNameRu())
            {
            case "Структура": //$NON-NLS-1$
            {
                result += "Соответствие<Строка,?>()"; //$NON-NLS-1$
                break;
            }
            case "Соответствие": //$NON-NLS-1$
            {
                result += "Соответствие<?,?>()"; //$NON-NLS-1$
                break;
            }
            case "Массив": //$NON-NLS-1$
            {
                result += "Массив<?>()"; //$NON-NLS-1$
                break;
            }
            case "Запрос": //$NON-NLS-1$
            {
                result += "Запрос()"; //$NON-NLS-1$
                break;
            }
            default:
                result += "НеизвестныйТип_" + rightType.getNameRu() + "()"; //$NON-NLS-1$ //$NON-NLS-2$
            }

            return result;

        }
        else if (expression instanceof BinaryExpression)
        {
            return ReturnStringFromBinaryExpression((BinaryExpression)expression, configuration);
        }
        else if (expression instanceof UnaryExpression)
        {
            return "не " + ReturnStringFromExpression(((UnaryExpression)expression).getOperand(), configuration); //$NON-NLS-1$
        }
        else if (expression instanceof UndefinedLiteral)
        {
            return "Неопределено"; //$NON-NLS-1$

        }
        else if (expression instanceof BooleanLiteral)
        {
            return ((BooleanLiteral)expression).isIsTrue() ? "Истина" : "Ложь"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        else if (expression instanceof StringLiteral)
        {
            EList<String> lines = ((StringLiteral)expression).getLines();
            return String.join(Strings.newLine(), lines);
        }
        else if (expression instanceof NumberLiteral)
        {
            return ((NumberLiteral)expression).getValue().get(0);
        }
        else if (expression instanceof IndexAccess)
        {
            return ReturnStringFromExpression(((IndexAccess)expression).getSource(), configuration) + "[" //$NON-NLS-1$
                + ReturnStringFromExpression(((IndexAccess)expression).getIndex(), configuration) + "]"; //$NON-NLS-1$
        }
        else if (expression != null)
        {
            return ""; //$NON-NLS-1$
        }

        return ""; //$NON-NLS-1$
    }

    protected String ReturnStringFromFeatureAccess(FeatureAccess featureAccess, Configuration configuration)
    {
        String result = ""; //$NON-NLS-1$

        if (featureAccess instanceof DynamicFeatureAccess)
        {
            Expression source = ((DynamicFeatureAccess)featureAccess).getSource();

            result = ReturnStringFromExpression(source, configuration) + "." + featureAccess.getName(); //$NON-NLS-1$
        }
        else
        {
            result = featureAccess.getName();
        }

        return result;
    }

    protected String ReturnStringFromStatement(Statement statement, List<String> usedVariables,
        Configuration configuration)
    {
        String result = ""; //$NON-NLS-1$

        if (statement instanceof SimpleStatement)
        {
            Expression leftExpression = ((SimpleStatement)statement).getLeft();
            Expression rightExpression = ((SimpleStatement)statement).getRight();

            result = ReturnStringFromExpression(leftExpression, configuration);

            if (leftExpression instanceof StaticFeatureAccess)
            {
                String variableName = ((StaticFeatureAccess)leftExpression).getName();
                if (!usedVariables.contains(variableName))
                {
                    result = "пер " + result;
                    usedVariables.add(variableName);
                }
            }

            if (rightExpression != null)
            {
                if (leftExpression instanceof Invocation)
                {
                    result += "."; //$NON-NLS-1$
                }
                else if (leftExpression instanceof StaticFeatureAccess
                    || leftExpression instanceof DynamicFeatureAccess)
                {
                    result += " = "; //$NON-NLS-1$
                }

                result += ReturnStringFromExpression(rightExpression, configuration);
            }

        }
        else if (statement instanceof ReturnStatement)
        {
            Expression returnExpression = ((ReturnStatement)statement).getExpression();

            result = "возврат"; //$NON-NLS-1$
            result += " " + ReturnStringFromExpression(returnExpression, configuration); //$NON-NLS-1$

        }
        else if (statement instanceof IfStatement)
        {
            Conditional ifPart = ((IfStatement)statement).getIfPart();
            Expression ifPredicate = ifPart.getPredicate();

            result = "если "; //$NON-NLS-1$
            result += ReturnStringFromExpression(ifPredicate, configuration) + Strings.newLine();
            result += "\t;" + Strings.newLine(); //$NON-NLS-1$
        }
        else if (statement instanceof WhileStatement)
        {
            Expression predicate = ((WhileStatement)statement).getPredicate();

            result = "пока "; //$NON-NLS-1$
            result += ReturnStringFromExpression(predicate, configuration);
            result += Strings.newLine();
            result += "\t;" + Strings.newLine(); //$NON-NLS-1$
        }
        else if (statement instanceof ForToStatement)
        {
            StaticFeatureAccess variableAccess = ((ForToStatement)statement).getVariableAccess();
            Expression initializer = ((ForToStatement)statement).getInitializer();
            Expression bound = ((ForToStatement)statement).getBound();

            result = "для "; //$NON-NLS-1$
            result += ReturnStringFromExpression(variableAccess, configuration);
            result += " = "; //$NON-NLS-1$
            result += ReturnStringFromExpression(initializer, configuration);
            result += " по "; //$NON-NLS-1$
            result += ReturnStringFromExpression(bound, configuration);
            result += Strings.newLine();
            result += "\t;" + Strings.newLine(); //$NON-NLS-1$
        }
        else if (statement instanceof ForEachStatement)
        {
            StaticFeatureAccess variableAccess = ((ForEachStatement)statement).getVariableAccess();
            Expression collection = ((ForEachStatement)statement).getCollection();

            result = "для "; //$NON-NLS-1$
            result += ReturnStringFromExpression(variableAccess, configuration);
            result += " из "; //$NON-NLS-1$
            result += ReturnStringFromExpression(collection, configuration);
            result += Strings.newLine();
            result += "\t;" + Strings.newLine(); //$NON-NLS-1$
        }
        else if (statement instanceof TryExceptStatement)
        {
            result = "попытка" + Strings.newLine(); //$NON-NLS-1$
            result += "\tпоймать искл: любой" + Strings.newLine(); //$NON-NLS-1$
            result += "\t;" + Strings.newLine(); //$NON-NLS-1$
        }
        else if (statement instanceof ExecuteStatement)
        {
            result = "выполнить()" + Strings.newLine(); //$NON-NLS-1$
        }
        else if (statement instanceof RaiseStatement)
        {
            result = "выбросить новый Исключение()" + Strings.newLine(); //$NON-NLS-1$
        }
        else if (statement instanceof EmptyStatement)
        {
            result = Strings.newLine() + Strings.newLine();
        }
        else if (statement != null)
        {
            result = ""; //$NON-NLS-1$
        }

        return result;
    }
}
