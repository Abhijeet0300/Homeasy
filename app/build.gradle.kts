plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "io.homeasy.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.homeasy.app"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        ndk{
            abiFilters.addAll(setOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
        }

        packaging{
            resources{
                pickFirsts += setOf(
                    "lib/*/libc++_shared.so",
                    "lib/*/libyuv.so",
                    "lib/*/libopenh264.so"
                )
            }
        }

        configurations.all {
            exclude(group = "com.thingclips.smart", module = "thingsmart-modularCampAnno")
        }

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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

//    kotlinOptions {
//        jvmTarget = "11"
//    }
//
    kotlin{
        jvmToolchain(21)
    }


    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //google fonts dependency
    implementation(libs.androidx.ui.text.google.fonts)

    //View model compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //Lifecycle for compose
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.extensions)

    //Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.android)

    //Dagger Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //tuya integration
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))
    implementation (libs.fastjson)
    implementation (libs.okhttp.urlconnection)
    implementation(libs.thingsmart)

    //Navigation compose
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}