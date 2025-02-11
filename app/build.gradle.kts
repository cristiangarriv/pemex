// Plugins necesarios para el proyecto
plugins {
    id("com.android.application") version "8.8.0" // Actualizar si hay una versión más reciente
    id("org.jetbrains.kotlin.android") version "1.9.10" // Actualizar si hay una versión más reciente
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

    // Excluir archivos no necesarios en el empaquetado
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
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.0.0")) // Actualizar si hay una versión más reciente
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-storage")

    // AndroidX
    implementation("androidx.appcompat:appcompat:1.6.1") // Actualizar si hay una versión más reciente
    implementation("com.google.android.material:material:1.9.0") // Actualizar si hay una versión más reciente
    implementation("androidx.activity:activity-ktx:1.7.2") // Actualizar si hay una versión más reciente
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Actualizar si hay una versión más reciente

    // Pruebas
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // iText para generar PDFs
    implementation("com.itextpdf:itext7-core:7.2.3")

    // Captura de firmas digitales
    implementation("com.github.gcacace:signature-pad:1.3.1")

    // Eliminar Apache PDFBox para evitar conflictos con iText
    // implementation("org.apache.pdfbox:pdfbox:2.0.27")
}