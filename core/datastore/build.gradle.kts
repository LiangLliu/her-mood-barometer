plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.hilt)
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
    namespace = "com.lianglliu.hermoodbarometer.core.datastore"
}

dependencies {
    api(projects.core.datastoreProto)
    api(projects.core.model)
    api(libs.androidx.dataStore)

    implementation(projects.core.common)
}