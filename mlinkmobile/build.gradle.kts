plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.detekt)
    id("kotlin-parcelize")
}

android {
    namespace = "com.mimeda.mlinkmobile"
    compileSdk = 34

    android.buildFeatures.buildConfig = true

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", "\"https://api.canerture.com/books/\"")
            buildConfigField("String","VERSION_NAME","\"${defaultConfig.versionName}\"")
        }
        release {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", "\"prod\"")
            buildConfigField("String","VERSION_NAME","\"${defaultConfig.versionName}\"")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.fuel)
    implementation(libs.gson)
}

detekt {
    autoCorrect = true
    buildUponDefaultConfig = true
    config = rootProject.files("$rootDir/config/detekt/detekt.yml")
    baseline = file("detekt-baseline.xml")
}