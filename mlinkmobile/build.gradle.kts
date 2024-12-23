plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.detekt)
    id("maven-publish")
}
group = "com.mimeda.mlink"

android {
    namespace = "com.mimeda.mlink"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildFeatures {
            buildConfig = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", "\"https://bidding-eventcollector.azurewebsites.net\"")
            buildConfigField("String", "VERSION_NAME", "\"1.0.2\"")
        }
        release {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", "\"https://bidding-eventcollector.azurewebsites.net\"")
            buildConfigField("String", "VERSION_NAME", "\"1.0.2\"")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
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
    config.setFrom(file("$rootDir/config/detekt/detekt.yml"))
    parallel = true
    autoCorrect = true
    buildUponDefaultConfig = true
}

afterEvaluate {
    android.libraryVariants.forEach {
        publishing {
            publications {
                register<MavenPublication>(it.name) {
                    from(components.findByName(it.name))

                    groupId = "com.mimeda.mlink"
                    artifactId = "android"
                    version = "1.0.2"
                }
            }
        }
    }
}
