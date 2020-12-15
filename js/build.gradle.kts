plugins {
    id("org.jetbrains.kotlin.js")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-js"))
}

val copySources by tasks.registering(Sync::class) {
    from("../src")
    into("$buildDir/src")
}

kotlin {
    js(IR) {
        nodejs {
        }
        binaries.executable()
    }
    sourceSets["main"].apply {
        kotlin.srcDirs("$buildDir/src", "../srcJs")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>> {
    dependsOn(copySources)
}


