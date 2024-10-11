plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
//    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version "2.0.0"
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            //documentacion
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            implementation(libs.androidx.navigation.compose)
            implementation( "androidx.compose.material:material-icons-extended:1.7.3")
            implementation( "androidx.compose.foundation:foundation:1.0.0")
            implementation ("androidx.compose.ui:ui:1.0.0")

            //firebase
            implementation(libs.firebase.bom)
            implementation(libs.firebase.firestore)
            implementation(libs.firebase.common)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.firebase.auth.ktx)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
            implementation(libs.firebase.common.ktx)
            implementation(libs.okhttp)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.lifecycle.viewmodel.android)
        }
        iosMain.dependencies {
//            implementation(libs.ktor.client.ios)
            implementation(libs.ktor.client.darwin)
        }
        jvmMain {
            dependencies {
                implementation(libs.androidx.lifecycle.viewmodel.desktop)
            }
        }
    }
}

android {
    namespace = "com.jasso.inteligenciaenventas"
    compileSdk = 34
    defaultConfig {
        minSdk = 28
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.firebase.auth.ktx)
}
