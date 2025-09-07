import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.spacECE.spaceceedu"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.spacECE.spaceceedu"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true
            isShrinkResources = false
            isMinifyEnabled = false
            isCrunchPngs = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isDebuggable = false
            isShrinkResources = true
            isMinifyEnabled = true
            isCrunchPngs = true
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
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    bundle {
        language {
            enableSplit = true
        }
        density {
            enableSplit = true
        }
        abi {
            enableSplit = true
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // UI Components
    implementation(libs.androidx.gridlayout)
    implementation(libs.core)
    implementation(libs.carouselview)
    implementation(libs.circular.progress.view)

    // Material Design & Animations
    implementation(libs.slidetoact)
    implementation(libs.checkout)

    // Screen & Dimension Helpers
    implementation(libs.ssp.android)
    implementation(libs.sdp.android)

    // Lifecycle
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.tools.core)

    // Firebase & Google Services
    implementation(libs.firebase.messaging)
    implementation(libs.play.services.auth)
    implementation(libs.play.services.base)

    // Networking & API
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.volley)

    // Media & Images
    implementation(libs.imagepicker)
    implementation(libs.picasso)
    implementation(libs.glide)

    // Charts & Graphs
    implementation(libs.anychart.android)
    implementation(libs.mpandroidchart)

    // AR / 3D / Video
    implementation(libs.filament.android)
    implementation(libs.full.sdk)

    // Payment
    implementation(libs.instamojo.sdk)
}