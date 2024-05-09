plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.tatanstudios.eltuncazometapan"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tatanstudios.eltuncazometapan"
        minSdk = 25
        targetSdk = 34
        versionCode = 5
        versionName = "1.0.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.navigation:navigation-fragment:2.7.6")
    implementation("androidx.navigation:navigation-ui:2.7.6")

    implementation("com.squareup.retrofit2:adapter-rxjava2:2.5.0") //rxjava
    implementation("com.squareup.retrofit2:converter-gson:2.5.0") //rxjava
    implementation("io.reactivex.rxjava2:rxandroid:2.1.0") //rxjava
    implementation("io.reactivex.rxjava2:rxjava:2.2.2") //rxjava

    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.github.GrenderG:Toasty:1.5.2")

   // implementation("io.github.tutorialsandroid:kalertdialog:20.4.8")

    implementation("com.github.bumptech.glide:glide:4.13.2") // glide imagenes
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.2") // glide soporte

    implementation("com.facebook.shimmer:shimmer:0.5.0")

    implementation("com.github.travijuu:numberpicker:1.0.7")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.github.angads25:toggle:1.1.0")


    implementation("com.synnapps:carouselview:0.1.5")
    implementation("com.github.jama5262:CarouselView:1.2.2")


    // imagen circular
    implementation("de.hdodenhof:circleimageview:3.0.0")

    implementation("io.github.tutorialsandroid:kalertdialog:20.4.8")
    implementation("com.github.TutorialsAndroid:progressx:v6.0.19") //required for kalertdialog lib

    // servicios de google maps
    implementation("com.google.android.gms:play-services-maps:17.0.1")
    implementation("com.google.maps.android:android-maps-utils:0.5+")
    implementation("com.google.android.libraries.places:places:2.4.0")


    // manejador de varias lineas de productos
    implementation("io.github.luizgrp.sectionedrecyclerviewadapter:sectionedrecyclerviewadapter:3.0.0")



    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}