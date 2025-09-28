plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.lianglliu.hermoodbarometer.core.network"
}

dependencies {
    api(projects.core.common)
    api(projects.core.model)

    implementation(libs.kotlinx.serialization.json)
    implementation(platform(libs.ktor.bom))
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.resources)
    implementation(libs.ktor.serialization.kotlinx.json)
}
