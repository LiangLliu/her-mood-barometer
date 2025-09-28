plugins {
    `kotlin-dsl`
}

group = "com.lianglliu.hermoodbarometer.buildlogic"

kotlin {
    jvmToolchain(21)
}

dependencies {
    compileOnly(libs.android.gradleApiPlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.firebase.crashlytics.gradlePlugin)
    compileOnly(libs.firebase.performance.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = libs.plugins.app.android.application.compose.get().pluginId
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = libs.plugins.app.android.application.asProvider().get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = libs.plugins.app.android.library.compose.get().pluginId
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.app.android.library.asProvider().get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = libs.plugins.app.android.feature.get().pluginId
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidTest") {
            id = libs.plugins.app.android.test.get().pluginId
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("androidRoom") {
            id = libs.plugins.app.android.room.get().pluginId
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidFirebase") {
            id = libs.plugins.app.android.application.firebase.get().pluginId
            implementationClass = "AndroidApplicationFirebaseConventionPlugin"
        }
        register("androidFlavors") {
            id = libs.plugins.app.android.application.flavors.get().pluginId
            implementationClass = "AndroidApplicationFlavorsConventionPlugin"
        }
        register("hilt") {
            id = libs.plugins.app.hilt.get().pluginId
            implementationClass = "HiltConventionPlugin"
        }
        register("jvmLibrary") {
            id = libs.plugins.app.jvm.library.get().pluginId
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
