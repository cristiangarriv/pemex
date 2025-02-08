// Plugins necesarios para el proyecto
plugins {
    id("com.android.application") version "8.8.0"
    id("org.jetbrains.kotlin.android") version "1.9.10"
    id("com.google.gms.google-services")
}

// Configuración de Android
android {
    namespace = "com.example.crudfirebase"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.crudfirebase"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    viewBinding {
        enable = true
    }

    buildFeatures {
        dataBinding = true
    }

    // 🛠 **Corrección: Uso de `resources.excludes.add()` en lugar de `exclude(...)`**
    packaging {
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("META-INF/license")
        resources.excludes.add("META-INF/LICENSE.txt")
    }
}

// Dependencias del proyecto
dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))

    // Servicios de Firebase
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-storage")

    // Dependencias de AndroidX
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Dependencias para pruebas
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //  Biblioteca iText para generar PDFs
    implementation("com.itextpdf:itext7-core:7.2.3")

    // ✍ Biblioteca para capturar firmas digitales
    implementation("com.github.gcacace:signature-pad:1.3.1")

    //  **Apache PDFBox** (Evita conflictos con iText y Firebase)
    implementation("org.apache.pdfbox:pdfbox:2.0.27")
}