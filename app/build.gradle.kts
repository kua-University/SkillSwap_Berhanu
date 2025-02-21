plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.skillswap"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.skillswap"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.material) // Or the latest version
    implementation(libs.appcompat)
    implementation(libs.navigation.fragment)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.swiperefreshlayout) // Or the latest versio
    testImplementation(libs.junit) // Or the latest version
    implementation ("androidx.recyclerview:recyclerview:1.3.1") // Use the latest version
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation ("com.google.firebase:firebase-firestore-ktx:24.10.1") // Or the latest version
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.2") // Or the latest version
    // ... other dependencies ...
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.2")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")


}