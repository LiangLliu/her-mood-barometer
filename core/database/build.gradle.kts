plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.room)
    alias(libs.plugins.app.hilt)
}

android {
    namespace = "com.lianglliu.hermoodbarometer.core.database"
}

dependencies {
    api(projects.core.model)

    implementation(libs.kotlinx.datetime)
    implementation(libs.zip4j)
}
