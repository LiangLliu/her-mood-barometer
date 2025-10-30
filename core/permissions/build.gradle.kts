plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.library.compose)
}

android {
    namespace = "com.lianglliu.hermoodbarometer.core.permissions"
}

dependencies {
    api(projects.core.designsystem)
    api(projects.core.locales)

    implementation(libs.androidx.core)
}
