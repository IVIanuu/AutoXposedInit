@file:Suppress("ClassName", "unused")

object Versions {
    // android
    const val compileSdk = 28
    const val minSdk = 14
    const val targetSdk = 28
    const val versionCode = 1
    const val versionName = "1.0"

    const val androidGradlePlugin = "3.1.2"

    const val autoService = "1.0-rc3"

    const val kotlin = "1.2.41"
    const val mavenGradlePlugin = "2.1"
    const val support = "28.0.0-alpha3"
    const val xposed = "82"
}

object Deps {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"

    const val autoService = "com.google.auto.service:auto-service:${Versions.autoService}"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    const val mavenGradlePlugin = "com.github.dcendents:android-maven-gradle-plugin:${Versions.mavenGradlePlugin}"

    const val supportAppCompat = "com.android.support:appcompat-v7:${Versions.support}"

    const val xposed = "de.robv.android.xposed:api:${Versions.xposed}"
}