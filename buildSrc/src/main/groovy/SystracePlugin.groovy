import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class SystracePlugin implements Plugin<Project> {

    private static final String TAG = "SystracePlugin";

    @Override
    void apply(Project project) {
        project.extensions.create("systrace", SystraceExtension)
        if (!project.plugins.hasPlugin("com.android.application"))
            throw new GradleException("systrace plugin android application needed")
        project.afterEvaluate {
            def android = project.extensions.android
            def configuration = project.systrace

        }
    }
}