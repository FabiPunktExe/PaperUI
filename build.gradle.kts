plugins {
    id("java")
    kotlin("jvm") version "2.2.21"
    id("io.papermc.paperweight.userdev") version "2.0.0-SNAPSHOT"
    id("maven-publish")
}

group = "de.fabiexe"
version = "1.1.2"

repositories {
    mavenCentral()
    maven("https://repo.codemc.io/repository/maven-releases")
}

dependencies {
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")
    compileOnly("com.github.retrooper:packetevents-spigot:2.10.1")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release = 21
    }

    processResources {
        filesMatching("paper-plugin.yml") {
            expand(mapOf("version" to version))
        }
    }

    jar {
        archiveFileName = "PaperUI-${version}-unobf.jar"
    }

    reobfJar {
        outputJar = file("build/libs/PaperUI-${version}.jar")
    }

    assemble {
        dependsOn(reobfJar)
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
