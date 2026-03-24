import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.lianglliu.hermoodbarometer.configureFlavors
import com.lianglliu.hermoodbarometer.configureKotlinAndroid
import com.lianglliu.hermoodbarometer.disableUnnecessaryAndroidTests
import com.lianglliu.hermoodbarometer.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")

            val targetSdk = libs.findVersion("androidTargetSdk").get().toString().toInt()
            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                testOptions.targetSdk = targetSdk
                lint.targetSdk = targetSdk
                configureFlavors(this)
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                disableUnnecessaryAndroidTests(target)
            }
            dependencies {
                "testImplementation"(kotlin("test"))

                "implementation"(libs.findLibrary("androidx.tracing").get())
                "implementation"(libs.findLibrary("timber").get())
            }
        }
    }
}
