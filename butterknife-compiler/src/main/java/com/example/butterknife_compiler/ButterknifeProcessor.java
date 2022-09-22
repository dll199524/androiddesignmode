package com.example.butterknife_compiler;


import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class ButterknifeProcessor extends AbstractProcessor {

    private Filer filer;
    private Elements elementsUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        elementsUtils = processingEnv.getElementUtils();
    }

    /**
     * 用来指定支持的 SourceVersion
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 给到需要处理的注解
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupprotedAnnotation())
            types.add(annotation.getCanonicalName());
        return types;
    }

    private Set<Class<? extends Annotation>> getSupprotedAnnotation() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(BindView.class);
        return annotations;
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        //解析生成List<Element>
        Map<Element, List<Element>> elementsMap = new LinkedHashMap<>();
        for (Element element : elements) {
            Element enclosingElement = element.getEnclosingElement();
            List<Element> viewBindElements = elementsMap.get(enclosingElement);
            if (viewBindElements == null) {
                viewBindElements = new ArrayList<>();
                elementsMap.put(element, viewBindElements);
            }
            viewBindElements.add(element);
        }

        //生成代码
        for (Map.Entry<Element, List<Element>> entry : elementsMap.entrySet()) {
            Element enclosingElement = entry.getKey();
            List<Element> annotations = entry.getValue();
            String activityNameStr = enclosingElement.getSimpleName().toString();
            ClassName activityName = ClassName.bestGuess(activityNameStr);
            ClassName unbinderName = ClassName.get("com.example.butterknife", "Unbinder");

            TypeSpec.Builder classBulider = TypeSpec.classBuilder(activityName + "_ViewBinding")
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                    .addSuperinterface(unbinderName)
                    .addField(activityName, "target", Modifier.PRIVATE);

            MethodSpec.Builder constructMethod = MethodSpec.constructorBuilder()
                    .addParameter(activityName, "target")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("this.target = target");

            ClassName callSuperClass = ClassName.get("andoridx.annotation", "CallSuper");
            MethodSpec.Builder unbindMethod = MethodSpec.methodBuilder("unbind")
                    .addAnnotation(Override.class)
                    .addAnnotation(callSuperClass)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addStatement("$T target = this.target", activityName)
                    .addStatement("if (target == null) throw new IllegalStateException(\"Bindings already cleared. target is null \");");

            for (Element annotation : annotations) {
                String annotationName = annotation.getSimpleName().toString();
                ClassName utilName = ClassName.get("com.example.designmode", "Utlis");
                int viewId = annotation.getAnnotation(BindView.class).value();

                constructMethod.addStatement("target.$L = $T.findViewById(target, %L);", annotationName, utilName, viewId);
                unbindMethod.addStatement("target.$L = null;", annotationName);
            }
            classBulider.addMethod(constructMethod.build());
            classBulider.addMethod(unbindMethod.build());

            try {
                String packageName = elementsUtils.getPackageOf(enclosingElement).getQualifiedName().toString();
                JavaFile.builder(packageName, classBulider.build())
                        .addFileComment("由javapot自动生成请勿删除")
                        .build()
                        .writeTo(filer);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
