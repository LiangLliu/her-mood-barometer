plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.hilt)
}

android {
    namespace = "com.lianglliu.hermoodbarometer.core.data"
}

dependencies {
    api(projects.core.common)
    api(projects.core.database)
    api(projects.core.datastore)
    api(projects.core.network)

    implementation(projects.core.designsystem)
    implementation(projects.core.notifications)
    implementation(projects.core.shortcuts)

    implementation(libs.androidx.appcompat)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.play.app.update)
    implementation(libs.play.app.update.ktx)
    implementation(libs.play.review)
    implementation(libs.play.review.ktx)
}
