plugins {


    id("org.jetbrains.intellij") version "1.1.4"
    java
    kotlin("jvm") version "1.5.21"
    id("org.jetbrains.compose") version "1.0.0-alpha3"
    id("idea")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.0")
    testImplementation(platform("org.junit:junit-bom:5.8.0"))
    // https://mvnrepository.com/artifact/com.squareup/javapoet
    implementation("com.squareup:javapoet:1.13.0")
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.21")
}


tasks.test {
    useJUnitPlatform()
}


// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2021.2")
    plugins.add("java")
}

tasks.patchPluginXml {
    changeNotes.set(
        """
      Add change notes here.<br>
      <em>most HTML tags may be used</em>"""
    )
}

