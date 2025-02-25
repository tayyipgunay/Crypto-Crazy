// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
   id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.8.0" apply false  // Safe Args plugin
    id("org.jetbrains.kotlin.kapt") version "1.9.0" apply false  // Kotlin Kapt plugin


}
buildscript {
    dependencies {
        // Hilt Gradle Plugin classpath'i ekliyoruz
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
    }
}