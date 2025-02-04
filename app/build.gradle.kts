import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
}

android {
    namespace = "com.mayad7474.mazaady_task"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mayad7474.mazaady_task"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            val releaseProp = Properties()
            releaseProp.load(FileInputStream("$rootDir/configs/release.properties"))
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
            buildConfigField("String", "BASE_URL", releaseProp.getProperty("BASE_URL"))
            buildConfigField("String", "API_KEY_HEADER", releaseProp.getProperty("API_KEY_HEADER"))
            buildConfigField("String", "API_KEY_VALUE", releaseProp.getProperty("API_KEY_VALUE"))
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            val debugProp = Properties()
            debugProp.load(FileInputStream("$rootDir/configs/develop.properties"))
            isMinifyEnabled = false
            isDebuggable = true
            manifestPlaceholders["app_name"] = debugProp.getProperty("APP_NAME")
            applicationIdSuffix = debugProp.getProperty("APPLICATION_ID_SUFFIX")
            buildConfigField("String", "BASE_URL", debugProp.getProperty("BASE_URL"))
            buildConfigField("String", "API_KEY_HEADER", debugProp.getProperty("API_KEY_HEADER"))
            buildConfigField("String", "API_KEY_VALUE", debugProp.getProperty("API_KEY_VALUE"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
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

    implementation(libs.activityExt)
    implementation(libs.fragmentExt)
    implementation(libs.viewModelExt)

    implementation(libs.navFragmentExt)
    implementation(libs.navExt)
    implementation(libs.navDynamicFeaturesExt)
    implementation(libs.navRuntimeExt)

    // Hilt - dependency injection
    implementation(libs.hilt.android)
    // hilt kapt - annotation processor — hilt ksp is in alpha
    kapt(libs.hilt.compiler)

    // Retrofit - make type safe requests
    implementation(libs.retrofit)
    implementation(libs.converter)

    // okhttp - fire http calls
    implementation(libs.okhttp)
    // logging interceptor - log calls for debugging
    implementation(libs.logging.interceptor)
    // converter - make retrofit use serialization json
    implementation(libs.converter.kotlinx.serialization)
    // JSON - serialize Kotlin objects <-> JSON
    implementation(libs.kotlinx.serialization.json)

    // gson
    implementation(libs.gson)

    implementation(libs.sdp)
    implementation(libs.ssp)
    implementation(libs.circleImage)
    implementation(libs.datatable)

    // testing
    implementation(libs.mockk)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.truth)

}
kapt {
    correctErrorTypes = true
}