/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.protobuf)
    alias(libs.plugins.gradle.license.report)
    alias(libs.plugins.owasp.dependencycheck)
}

android {
    namespace = "com.fairphone.spring.launcher"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.thayyil.spring.launcher"
        minSdk = 35
        targetSdk = 35
        versionCode = 111
        versionName = "0.1.10.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            abiFilters.add("arm64-v8a")
        }
        @Suppress("UnstableApiUsage")
        androidResources.localeFilters.addAll(listOf("en", "da", "de", "es", "fr", "it", "nl", "no", "pt", "sv"))
        base.archivesName = "SpringLauncher_$versionName"
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/java")
            kotlin.srcDirs("src/main/kotlin")
            res.srcDirs("src/main/res")
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.addAll(
                "-Xstring-concat=inline",
                "-Xwhen-guards",
                "-opt-in=kotlin.time.ExperimentalTime",
            )
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    applicationVariants.all { variant ->
        variant.outputs.all { output ->
            (output as? BaseVariantOutputImpl)?.apply {
                outputFileName = "SpringLauncher_${variant.buildType.name}.apk"
            }
            true
        }
    }
}

dependencies {
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.icons)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.work.runtime)
    implementation(libs.coil)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.kotlinx.serialization)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.protobuf.kotlin.lite)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.robolectric)
    testImplementation(libs.koin.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

// OWASP dependency check configuration
dependencyCheck {
    nvd {
        apiKey = System.getenv("NVD_API_KEY")
    }
    failBuildOnCVSS = 7.0f
    suppressionFile = "config/owasp/suppressions.xml"
    scanConfigurations = listOf("productionReleaseeleaseCompileClasspath")
}

licenseReport {
    allowedLicensesFile = file("$rootDir/config/licenses/allowed-licenses.json")
}