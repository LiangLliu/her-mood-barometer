plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.hilt)
}

android {
    namespace = "com.lianglliu.hermoodbarometer.core.notifications"
}

dependencies {
    api(projects.core.common)
    api(projects.core.model)

    implementation(projects.core.locales)
    implementation(projects.core.ui)
}
