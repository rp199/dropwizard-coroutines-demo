plugins {
    id("com.rp199.kotlin-application-conventions")
}

dependencies {
    implementation(libs.bundles.logging)
    implementation(libs.dropwizard.core)
    implementation(libs.jackson.kotlin)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.bundles.test)
}

application {
    // Define the main class for the application.
    mainClass.set("com.rp199.AppKt")
}
