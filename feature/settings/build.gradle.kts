plugins {
    alias(libs.plugins.app.android.feature)
    alias(libs.plugins.app.android.library.compose)
    alias(libs.plugins.aboutlibraries)
}

android {
    namespace = "com.lianglliu.hermoodbarometer.feature.settings"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.locales)

    implementation(libs.aboutlibraries.core)
    implementation(libs.aboutlibraries.compose)
    implementation(libs.androidx.browser)
}