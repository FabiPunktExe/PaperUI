plugins {
    id("java")
    kotlin("jvm") version "2.2.10"
    id("io.papermc.paperweight.userdev") version "2.0.0-SNAPSHOT"
    id("maven-publish")
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "de.fabiexe"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")
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
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        archiveFileName = "PaperUI-${version}-unobf.jar"
    }

    reobfJar {
        outputJar = file("build/libs/PaperUI-${version}.jar")
    }

    assemble {
        dependsOn(reobfJar)
    }

    runServer {
        minecraftVersion("1.21.8")
        systemProperty("PAPERUI_ENABLE_DEV_COMMANDS", "true")
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
