plugins {
    application
    java
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
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("cz.cardgames.fortressduel.Main")
    applicationDefaultJvmArgs = listOf("-Dorg.slf4j.simpleLogger.defaultLogLevel=debug")
}