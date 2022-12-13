package com.designmode.plugin;


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
        project.getExtensions().create("patch", PatchExtension.class);
        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                final PatchExtension patchExtension =
                        project.getExtensions().findByType(PatchExtension.class);
                boolean isDebug = patchExtension.isDebug;

            }
        });
    }
}
