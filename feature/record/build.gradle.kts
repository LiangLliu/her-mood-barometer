plugins {
    alias(libs.plugins.app.android.feature)
    alias(libs.plugins.app.android.library.compose)
}

android {
    namespace = "com.lianglliu.hermoodbarometer.feature.record"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.locales)
}