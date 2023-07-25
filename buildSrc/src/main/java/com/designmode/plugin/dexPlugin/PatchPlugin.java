package com.designmode.plugin.dexPlugin;


import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.ApplicationVariant;

import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ApplicationPlugin;


public class PatchPlugin implements Plugin<Project> {

    Project project;

    @Override
    public void apply(Project project) {
        if (project.getPlugins().hasPlugin(ApplicationPlugin.class))
            throw new GradleException("无法在非android application插件中使用热修复插件");
        this.project = project;
        //创建一个patch{}配置
        //就和引入了 apply plugin: 'com.android.application' 一样，可以配置android{}
        project.getExtensions().create("patch", PatchExtension.class);
        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                PatchExtension patchExtension = project.getExtensions().findByType(PatchExtension.class);
                boolean isDebug = patchExtension.isDebug;
                AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
                appExtension.getApplicationVariants().all(new Action<ApplicationVariant>() {
                    @Override
                    public void execute(ApplicationVariant applicationVariant) {
                        if (applicationVariant.getName().contains("debug") && !isDebug)
                            return;
                        configTasks(project, applicationVariant, patchExtension);
                    }
                });
            }
        });
    }


    void configTasks(final Project project, final ApplicationVariant variant,
                     final PatchExtension patchExtension) {

    }
}
