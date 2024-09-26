plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.kp.softsavvy.futeres.no_code"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kp.softsavvy.futeres.no_code"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a") // Include the architectures you need
        }
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

    // Remove ABI splits block entirely
    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs")
        }
    }
    packagingOptions {
        doNotStrip("lib/armeabi-v7a/libil2cpp.so")
        doNotStrip("lib/arm64-v8a/libil2cpp.so")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
//        viewBinding = true
        buildConfig = true
    }
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation(files("libs/unity-classes.jar"))
//    implementation(project(":unityLibrary"))
}