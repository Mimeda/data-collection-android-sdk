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
            buildConfigField("String", "EVENT_URL", "\"https://event.mlink.com.tr\"")
            buildConfigField("String", "PERFORMANCE_URL", "\"https://performance.mlink.com.tr\"")
            buildConfigField("String", "VERSION_NAME", "\"1.1.1\"")
        }
        release {
            isMinifyEnabled = false
            buildConfigField("String", "EVENT_URL", "\"https://event.mlink.com.tr\"")
            buildConfigField("String", "PERFORMANCE_URL", "\"https://performance.mlink.com.tr\"")
            buildConfigField("String", "VERSION_NAME", "\"1.1.1\"")
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
    implementation(libs.androidx.appcompat)

    implementation(libs.fuel)
    implementation(libs.gson)

    implementation(libs.encryptedSharedPreferences)
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
                    version = "1.1.1"
                }
            }
        }
    }
}
