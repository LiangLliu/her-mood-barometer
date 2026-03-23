import com.android.build.api.dsl.TestExtension
import com.lianglliu.hermoodbarometer.configureKotlinAndroid
import com.lianglliu.hermoodbarometer.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidTestConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.test")

            extensions.configure<TestExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk =
                    libs.findVersion("androidTargetSdk").get().toString().toInt()
            }
        }
    }
}
