/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.bsl.internal.yaml.writer;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.util.Strings;

import com._1c.g5.v8.dt.bsl.documentation.comment.BslCommentUtils;
import com._1c.g5.v8.dt.bsl.documentation.comment.BslDocumentationComment;
import com._1c.g5.v8.dt.bsl.documentation.comment.BslMultiLineCommentDocumentationProvider;
import com._1c.g5.v8.dt.bsl.documentation.comment.IDescriptionPart;
import com._1c.g5.v8.dt.bsl.documentation.comment.TextPart;
import com._1c.g5.v8.dt.bsl.model.DynamicFeatureAccess;
import com._1c.g5.v8.dt.bsl.model.Expression;
import com._1c.g5.v8.dt.bsl.model.FeatureAccess;
import com._1c.g5.v8.dt.bsl.model.FeatureEntry;
import com._1c.g5.v8.dt.bsl.model.FormalParam;
import com._1c.g5.v8.dt.bsl.model.Function;
import com._1c.g5.v8.dt.bsl.model.Invocation;
import com._1c.g5.v8.dt.bsl.model.Method;
import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.model.StaticFeatureAccess;
import com._1c.g5.v8.dt.bsl.model.util.BslUtil;
import com._1c.g5.v8.dt.bsl.resource.BslResource;
import com._1c.g5.v8.dt.bsl.resource.DynamicFeatureAccessComputer;
import com._1c.g5.v8.dt.bsl.resource.TypesComputer;
import com._1c.g5.v8.dt.core.platform.IConfigurationAware;
import com._1c.g5.v8.dt.core.platform.IV8Project;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;
import com._1c.g5.v8.dt.mcore.Environmental;
import com._1c.g5.v8.dt.mcore.TypeItem;
import com._1c.g5.v8.dt.mcore.util.Environments;
import com._1c.g5.v8.dt.metadata.mdclass.Configuration;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

public class ModuleWriter
{
    @Inject
    private IV8ProjectManager v8ProjectManager;

    private final BslMultiLineCommentDocumentationProvider commentProvider;
    private final DynamicFeatureAccessComputer dynamicFeatureAccessComputer;
    private final TypesComputer typesComputer;

    @Inject
    public ModuleWriter(BslMultiLineCommentDocumentationProvider commentProvider,
        DynamicFeatureAccessComputer dynamicFeatureAccessComputer, TypesComputer typesComputer)
    {
        this.commentProvider = commentProvider;
        this.dynamicFeatureAccessComputer = dynamicFeatureAccessComputer;
        this.typesComputer = typesComputer;
    }

    public void write(YamlStreamWriter writer, EObject eObject) throws ExportException
    {
        IV8Project v8Project = v8ProjectManager.getProject(eObject);

        Configuration configuration = ((IConfigurationAware)v8Project).getConfiguration();

        Module module = (Module)EcoreUtil2.resolve(eObject, configuration);
        ((BslResource)module.eResource()).setDeepAnalysis(true);
        EcoreUtil2.resolveLazyCrossReferences(module.eResource(), null);

        writer.writeElement("Имя", module.getUniqueName(), null); //$NON-NLS-1$
        List<Object> methodList = writer.addList("Методы"); //$NON-NLS-1$

        EList<Method> methods = module.allMethods();
        for (Method method : methods)
        {
            Map<String, Object> methodGroup = writer.addGroup(methodList);

            writer.writeElement("Имя", method.getName(), methodGroup); //$NON-NLS-1$

            BslDocumentationComment comment = BslCommentUtils.parseTemplateComment(method, true, commentProvider);

            String commentText = ""; //$NON-NLS-1$
            for (IDescriptionPart part : comment.getDescription().getParts())
            {
                if (part instanceof TextPart)
                {
                    String partText = ((TextPart)part).getText();
                    commentText += partText + Strings.newLine();
                }
            }
            writer.writeElement("Описание", commentText, methodGroup); //$NON-NLS-1$

            writer.writeElement("ОбластьВидимости", method.isExport() ? "Проект" : "Локально", methodGroup); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

            EList<FormalParam> formalParams = method.getFormalParams();

            if (!formalParams.isEmpty())
            {
                List<Object> paramList = writer.addList("Параметры", methodGroup); //$NON-NLS-1$

                for (FormalParam formalParam : formalParams)
                {
                    Map<String, Object> paramGroup = writer.addGroup(paramList);

                    writer.writeElement("Имя", formalParam.getName(), paramGroup); //$NON-NLS-1$

                    List<TypeItem> paramTypes = typesComputer.computeTypes(formalParam, method.environments());

                    HashSet<String> params = new HashSet<>();
                    boolean hasUndefined = false;
                    for (TypeItem paramType : paramTypes)
                    {
                        if (paramType.eIsProxy())
                        {
                            paramType = (TypeItem)EcoreUtil2.resolve(paramType, configuration);
                        }

                        String typeName = getTypeName(paramType.getNameRu());
                        switch (typeName)
                        {
                        case "?": //$NON-NLS-1$
                            hasUndefined = true;
                            break;

                        case "": //$NON-NLS-1$
                            break;

                        default:
                            params.add(typeName);
                        }
                    }

                    String paramText = ""; //$NON-NLS-1$
                    if (params.isEmpty())
                    {
                        paramText += "Произвольный"; //$NON-NLS-1$
                    }
                    else
                    {
                        paramText += String.join("|", params); //$NON-NLS-1$
                        if (hasUndefined)
                        {
                            paramText += "?"; //$NON-NLS-1$
                        }
                    }

                    writer.writeElement("Тип", paramText, paramGroup); //$NON-NLS-1$
                }
            }

            if (method instanceof Function)
            {
                List<TypeItem> paramTypes = typesComputer.computeTypes(method, method.environments());

                HashSet<String> params = new HashSet<>();
                boolean hasUndefined = false;
                for (TypeItem paramType : paramTypes)
                {
                    if (paramType.eIsProxy())
                    {
                        paramType = (TypeItem)EcoreUtil2.resolve(paramType, configuration);
                    }

                    String typeName = getTypeName(paramType.getNameRu());
                    switch (typeName)
                    {
                    case "?": //$NON-NLS-1$
                        hasUndefined = true;
                        break;

                    case "": //$NON-NLS-1$
                        break;

                    default:
                        params.add(typeName);
                    }
                }

                String paramText = ""; //$NON-NLS-1$
                if (params.isEmpty())
                {
                    paramText += "Произвольный"; //$NON-NLS-1$
                }
                else
                {
                    paramText += String.join("|", params); //$NON-NLS-1$
                    if (hasUndefined)
                    {
                        paramText += "?"; //$NON-NLS-1$
                    }
                }

                writer.writeElement("ВозвращаемыйТип", paramText, methodGroup); //$NON-NLS-1$
            }

            Environments methodEnvs = getActualEnvironments(method);

            List<StaticFeatureAccess> sfaList = EcoreUtil2.eAllOfType(method, StaticFeatureAccess.class);
            if (!sfaList.isEmpty())
            {
                Set<String> uniqueMethods = new HashSet<>();

                for (StaticFeatureAccess sfa : sfaList)
                {
                    if (BslUtil.getInvocation(sfa) == null)
                    {
                        continue;
                    }

                    EObject sourceMethod = getSourceMethod(sfa, methodEnvs);
                    if (!(sourceMethod instanceof Method))
                    {
                        continue;
                    }

                    uniqueMethods.add(sfa.getName());
                }

                List<Object> subList = writer.addList("ЛокальныеВызовы", methodGroup); //$NON-NLS-1$

                for (String str : uniqueMethods)
                {
                    Map<String, Object> subGroup = writer.addGroup(subList);
                    writer.writeElement("Имя", str, subGroup); //$NON-NLS-1$
                }

                writer.removeEmptyList("ЛокальныеВызовы", methodGroup); //$NON-NLS-1$
            }

            List<DynamicFeatureAccess> dfaList = EcoreUtil2.eAllOfType(method, DynamicFeatureAccess.class);
            if (!dfaList.isEmpty())
            {
                Set<String> uniqueMethods = new HashSet<>();

                for (DynamicFeatureAccess dfa : dfaList)
                {
                    if (BslUtil.getInvocation(dfa) == null)
                    {
                        continue;
                    }

                    EObject sourceMethod = getSourceMethod(dfa, methodEnvs);
                    if (!(sourceMethod instanceof com._1c.g5.v8.dt.bsl.model.BslContextDefMethod))
                    {
                        continue;
                    }

                    String sourceName = getSourceName(dfa.getSource());

                    uniqueMethods.add(String.join(".", sourceName, dfa.getName())); //$NON-NLS-1$
                }

                List<Object> subList = writer.addList("ВнешниеВызовы", methodGroup); //$NON-NLS-1$

                for (String str : uniqueMethods)
                {
                    String[] methodAndModule = str.split("[.]"); //$NON-NLS-1$

                    Map<String, Object> subGroup = writer.addGroup(subList);

                    writer.writeElement("Модуль", methodAndModule[0], subGroup); //$NON-NLS-1$
                    writer.writeElement("Имя", methodAndModule[1], subGroup); //$NON-NLS-1$
                }

                writer.removeEmptyList("ВнешниеВызовы", methodGroup); //$NON-NLS-1$
            }
        }
    }

    private String getSourceName(Expression source)
    {
        String result = ""; //$NON-NLS-1$

        if (source instanceof FeatureAccess)
        {
            result = ((FeatureAccess)source).getName();
        }

        else if (source instanceof Invocation)
        {
            result = getSourceName(((Invocation)source).getMethodAccess());
        }

        return result;
    }

    protected Environments getActualEnvironments(EObject object)
    {
        Environmental envs = EcoreUtil2.getContainerOfType(object, Environmental.class);
        if (envs == null)
        {
            return Environments.EMPTY;
        }

        return envs.environments();
    }

    protected EObject getSourceMethod(FeatureAccess object, Environments actualEnvs)
    {
        if (actualEnvs.isEmpty())
        {
            return null;
        }
        List<FeatureEntry> objects = dynamicFeatureAccessComputer.resolveObject(object, actualEnvs);
        for (FeatureEntry entry : objects)
        {
            EObject source = entry.getFeature();
            if (source instanceof Method || source instanceof com._1c.g5.v8.dt.mcore.Method)
            {
                return source;
            }
        }

        return null;
    }

    protected String getTypeName(String typeName)
    {
        switch (typeName)
        {
        case "Неопределено": //$NON-NLS-1$
            return "?"; //$NON-NLS-1$

        case "Произвольный": //$NON-NLS-1$
            return ""; //$NON-NLS-1$

        case "Массив": //$NON-NLS-1$
            return "Массив<Неизвестно>"; //$NON-NLS-1$

        case "ФиксированныйМассив": //$NON-NLS-1$
            return "ФиксированныйМассив<Неизвестно>"; //$NON-NLS-1$

        case "Соответствие": //$NON-NLS-1$
            return "Соответствие<Неизвестно,Неизвестно>"; //$NON-NLS-1$

        case "ФиксированноеСоответствие": //$NON-NLS-1$
            return "ФиксированноеСоответствие<Неизвестно,Неизвестно>"; //$NON-NLS-1$

        case "Структура": //$NON-NLS-1$
            return "Структура<Строка,Неизвестно>"; //$NON-NLS-1$

        case "ФиксированнаяСтруктура": //$NON-NLS-1$
            return "ФиксированнаяСтруктура<Строка,Неизвестно>"; //$NON-NLS-1$

        default:
            return typeName;

        }
    }

}
