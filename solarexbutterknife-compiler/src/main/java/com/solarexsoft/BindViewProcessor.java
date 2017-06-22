package com.solarexsoft;


import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 21/06/2017
 *    Desc:
 * </pre>
 */
@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {

    private Elements mElements;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElements = processingEnvironment.getElementUtils();
        mFiler = processingEnvironment.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(BindView.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Map<TypeElement, List<FieldViewBinding>> targetMap = new HashMap<>();
        FileUtils.print("--------------------------");
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        for (Element element : elements) {
            FileUtils.print("element: " + element.getSimpleName().toString());
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            FileUtils.print("enclosingElement: " + enclosingElement);
            List<FieldViewBinding> list = targetMap.get(enclosingElement);
            if (list == null) {
                list = new ArrayList<>();
                targetMap.put(enclosingElement, list);
            }
            String packageName = getPackageName(enclosingElement);
            int id = element.getAnnotation(BindView.class).value();
            String fieldName = element.getSimpleName().toString();
            TypeMirror typeMirror = element.asType();
            FieldViewBinding fieldViewBinding = new FieldViewBinding(fieldName, typeMirror, id);
            list.add(fieldViewBinding);
        }
        for (Map.Entry<TypeElement, List<FieldViewBinding>> typeElementListEntry : targetMap
                .entrySet()) {
            List<FieldViewBinding> list = typeElementListEntry.getValue();
            if (list == null || list.size() == 0) {
                continue;
            }
            TypeElement enclosingElement = typeElementListEntry.getKey();
            String packageName = getPackageName(enclosingElement);
            String complete = getClassName(enclosingElement, packageName);
            ClassName className = ClassName.bestGuess(complete);
            ClassName viewBinder = ClassName.get("com.solarexsoft.solarexbutterknife",
                    "ViewBinder");

            TypeSpec.Builder result = TypeSpec.classBuilder(complete + "$$ViewBinder")
                    .addModifiers(Modifier.PUBLIC)
                    .addTypeVariable(TypeVariableName.get("T", className))
                    .addSuperinterface(ParameterizedTypeName.get(viewBinder, className));

            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("bind")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addAnnotation(Override.class)
                    .addParameter(className, "target", Modifier.FINAL);

            for (int i = 0; i < list.size(); i++) {
                FieldViewBinding fieldViewBinding = list.get(i);
                String packageNameString = fieldViewBinding.getType().toString();
                ClassName viewClass = ClassName.bestGuess(packageNameString);
                methodBuilder.addStatement("target.$L=($T)target.findViewById($L)",
                        fieldViewBinding.getName(), viewClass, fieldViewBinding.getResId());
            }

            result.addMethod(methodBuilder.build());
            try {
                JavaFile.builder(packageName, result.build())
                        .addFileComment("auto generated by solarex-butterknife-compiler")
                        .build().writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String getClassName(TypeElement enclosingElement, String packageName) {
        int packageLength = packageName.length() + 1;
        return enclosingElement.getQualifiedName().toString().substring(packageLength).replace("" +
                ".", "$");
    }

    private String getPackageName(TypeElement enclosingElement) {
        return mElements.getPackageOf(enclosingElement).getQualifiedName().toString();
    }


}
