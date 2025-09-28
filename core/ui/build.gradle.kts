plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.library.compose)
}

android {
    namespace = "com.lianglliu.hermoodbarometer.core.ui"
}

dependencies {
    api(projects.core.analytics)
    api(projects.core.designsystem)
    api(projects.core.locales)
    api(projects.core.model)

    implementation(projects.core.common)

    implementation(libs.androidx.activity.compose)
    implementation(libs.lottie.compose)
    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)
    implementation(libs.vico.core)
}
