plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.library.compose)
}

android { namespace = "com.lianglliu.hermoodbarometer.core.designsystem" }

dependencies {
    api(projects.core.model)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.icons.extended)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.text.google.fonts)
    api(libs.androidx.compose.ui.util)
}
