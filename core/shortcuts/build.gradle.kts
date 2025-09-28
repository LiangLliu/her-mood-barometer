plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.hilt)
}

android {
    namespace = "com.lianglliu.hermoodbarometer.core.shortcuts"
}

dependencies {
    api(projects.core.common)
    api(projects.core.model)

    implementation(projects.core.locales)
}
