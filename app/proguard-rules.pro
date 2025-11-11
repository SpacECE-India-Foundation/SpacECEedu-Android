# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# --- Room (keep entities, DAOs, database) ---
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keep class * extends androidx.room.RoomOpenHelper
-keep @androidx.room.* class * { *; }

# --- Retrofit + Gson ---
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class com.google.gson.** { *; }
-keep class okhttp3.logging.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Retrofit models and interfaces (optional example):
-keep class com.tradeforsure.data.** { *; }
-keep class com.tradeforsure.util.** { *; }
-keep class com.tradeforsure.viewModel.** { *; }
-keep class android.text.Html { *; }

# --- Coroutines, Lifecycle ---
-dontwarn kotlinx.coroutines.**
-dontwarn androidx.lifecycle.**

# --- Glide (for image loading) ---
-keep class com.bumptech.glide.** { *; }
-keep interface com.bumptech.glide.** { *; }
-keep class com.bumptech.glide.annotation.** { *; }
-keep class com.bumptech.glide.generated.** { *; }

# --- Google Play Services (OTP/Auth/Updates) ---
-dontwarn com.google.android.gms.**
-dontwarn com.google.firebase.**

# --- Firebase ---
-keep class com.google.firebase.** { *; }
-keep class com.google.analytics.** { *; }

# --- Razorpay ---
-dontwarn com.razorpay.**
-keep class com.razorpay.** { *; }