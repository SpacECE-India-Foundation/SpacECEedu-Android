import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    id("kotlin-kapt")
    id("kotlin-parcelize")

        //id("com.android.application")

        // Add the Google services Gradle plugin
        id("com.google.gms.google-services")


}

android {
    namespace = "com.spacece.milestonetracker"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        targetSdk = 36

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    //Default Libraries
    implementation(libs.material)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //SwipeRefresh + ConstraintLayout
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)

    //CircleIndicator
    implementation(libs.circleindicator)

    //ShimmerLoader + Splash
    implementation(libs.shimmer)
    implementation(libs.androidx.core.splashscreen)

    //Room DB
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    //Coroutines + Lifecycle + ViewModel + LiveData
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.fragment.ktx)

    //Retrofit API Call
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Image Loading
    implementation(libs.glide)

    //view & text size
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)

    //check/update app + InAppReview
    implementation(libs.app.update)
    implementation(libs.play.app.update.ktx)

    // Razorpay
    implementation(libs.checkout)

    //UTube
    implementation(libs.core)


    //Mp Chart for graph
    implementation(libs.mpandroidchart)
    implementation(platform("com.google.firebase:firebase-bom:34.13.0"))
    implementation("com.google.firebase:firebase-analytics")
}