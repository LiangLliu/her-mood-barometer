import com.lianglliu.hermoodbarometer.AppBuildType

plugins {
    alias(libs.plugins.app.android.application)
    alias(libs.plugins.app.android.application.compose)
//    alias(libs.plugins.app.android.application.firebase)
    alias(libs.plugins.app.android.application.flavors)
    alias(libs.plugins.app.hilt)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.lianglliu.hermoodbarometer"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.lianglliu.hermoodbarometer"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            applicationIdSuffix = AppBuildType.DEBUG.applicationIdSuffix
            versionNameSuffix = "-debug"
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            applicationIdSuffix = AppBuildType.RELEASE.applicationIdSuffix
            baselineProfile.automaticGenerationDuringBuild = true
            signingConfig = signingConfigs.named("debug").get()
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    androidResources {
        generateLocaleConfig = true
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

baselineProfile {
    automaticGenerationDuringBuild = false
    dexLayoutOptimization = true
}

dependencies {
    implementation(projects.feature.record)
    implementation(projects.feature.statistics)
    implementation(projects.feature.settings)

    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(projects.core.ui)

    implementation(projects.work)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.tracing)
    implementation(libs.kotlinx.serialization.json)

    debugImplementation(libs.androidx.compose.ui.testManifest)

//    baselineProfile(projects.baselineprofile)
}