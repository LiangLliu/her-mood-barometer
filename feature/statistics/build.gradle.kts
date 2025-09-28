plugins {
    alias(libs.plugins.app.android.feature)
    alias(libs.plugins.app.android.library.compose)
}

android {
    namespace = "com.lianglliu.hermoodbarometer.feature.statistics"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.locales)

    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)
    implementation(libs.vico.core)
}