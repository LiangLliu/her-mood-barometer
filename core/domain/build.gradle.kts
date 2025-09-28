plugins {
    alias(libs.plugins.app.android.library)
}

android {
    namespace = "com.lianglliu.hermoodbarometer.core.domain"
}

dependencies {
    api(projects.core.data)
    api(projects.core.model)

    implementation(libs.javax.inject)
}