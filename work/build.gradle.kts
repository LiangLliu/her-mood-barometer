plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.hilt)
}

android {
    namespace = "com.lianglliu.hermoodbarometer.work"
}

dependencies {
    ksp(libs.hilt.ext.compiler)

    implementation(projects.core.data)

    implementation(libs.androidx.work.ktx)
    implementation(libs.hilt.ext.work)
}
