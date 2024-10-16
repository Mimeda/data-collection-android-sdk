plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.detekt)
    id("maven-publish")
}

android {
    namespace = "com.mimeda.sdk"
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
            buildConfigField("String", "BASE_URL", "\"https://collector.avvamobiledemo.com/im.gif?\"")
            buildConfigField("String","VERSION_NAME","\"1.0.0\"")
        }
        release {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", "\"https://collector.avvamobiledemo.com/im.gif?\"")
            buildConfigField("String","VERSION_NAME","\"1.0.0\"")
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

                    groupId = "com.mimeda.mlinkmobile"
                    artifactId = "android"
                    version = "1.0.0"
                }
            }
        }
    }
}