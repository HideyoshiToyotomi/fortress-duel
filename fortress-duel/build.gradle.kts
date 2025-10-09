import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // JSON (Jackson pro práci s kartami, uloženými v .json souborech)
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")

    // Logging (jednoduchý SLF4J backend)
    implementation("org.slf4j:slf4j-simple:2.0.16")

    // Testování (JUnit 5)
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")

    implementation("org.glassfish.tyrus:tyrus-server:2.1.4")
    implementation("org.glassfish.tyrus:tyrus-container-grizzly-server:2.1.4")
    implementation("com.h2database:h2:2.2.224")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("fortress-ws")
    archiveVersion.set("0.1.0")
    archiveClassifier.set("")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("cz.cardgames.fortressduel.adapters.ws.WsServerApp")
    applicationDefaultJvmArgs = listOf("-Dorg.slf4j.simpleLogger.defaultLogLevel=debug")
}


