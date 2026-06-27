plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.paperweight.userdev)
    `maven-publish`
}

group = "de.fabiexe"
version = "1.2.0"

repositories {
    mavenCentral()
    google()
    maven("https://repo.codemc.io/repository/maven-releases")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.get())
    api(libs.compose.runtime)
    api(libs.kotlinx.coroutines.core)
    compileOnly(libs.packetevents.spigot)
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release = 25
    }

    processResources {
        filesMatching("paper-plugin.yml") {
            expand("version" to version)
        }
    }

    jar {
        archiveFileName = "PaperUI-${version}.jar"
    }
}

publishing {
    repositories {
        maven("https://repo.diruptio.de/repository/maven-public-releases") {
            name = "DiruptioPublic"
            credentials {
                username = (System.getenv("DIRUPTIO_REPO_USERNAME") ?: project.findProperty("maven_username") ?: "").toString()
                password = (System.getenv("DIRUPTIO_REPO_PASSWORD") ?: project.findProperty("maven_password") ?: "").toString()
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            artifactId = "PaperUI"
            from(components["java"])
        }
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}
