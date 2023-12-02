plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.btlmp4"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.btlmp4"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")

//    Thêm thư viện cho ứng dụng
    implementation ("com.github.bumptech.glide:glide:4.16.0")
//      thư viện tải và hiển thị hình ảnh
    implementation ("com.google.android.exoplayer:exoplayer:2.18.7")
//    thư viện phát nội dung đa phương tiện
    implementation ("androidx.multidex:multidex:2.0.1")
//    Các thư viện Glide và Exoplayer tăng lượng lớn phương thức
//    thư viện chuyển đổi Java thành tệp tin DEX
//    giúp giảm kích thước ứng dụng và tăng hiệu suất trong môi trường tài nguyên hạn chế.
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0");
//    thư viện giúp thêm tính năng thêm tính năng "pull-to-refresh"
//    vào các giao diện người dùng, giúp người dùng có thể làm mới
//    nội dung bằng cách kéo xuống.

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}