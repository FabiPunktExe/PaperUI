plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.runPaper)
    alias(libs.plugins.minotaur)
    alias(libs.plugins.hangarPublish)
}

repositories {
    mavenCentral()
    google()
    maven("https://repo.codemc.io/repository/maven-releases")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.get())
    compileOnly(libs.packetevents.spigot)
    implementation(projects.paperUI)
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
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        archiveFileName = "PaperUI-${version}.jar"
    }

    runServer {
        minecraftVersion("26.2")
        systemProperty("PAPERUI_ENABLE_DEV_COMMANDS", "true")
    }
}

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    projectId = "JdoLDo8L"
    versionName = version as String
    versionNumber = version as String
    versionType = if (version.toString().contains("alpha")) "alpha"
    else if (version.toString().contains("beta")) "beta"
    else "release"
    uploadFile = tasks.reobfJar.get().outputJar.get()
    gameVersions = listOf("26.2")
    loaders = listOf("paper", "purpur")
}

hangarPublish {
    publications.register("plugin") {
        version = project.version as String
        id = "PaperUI"
        channel = "Release"

        apiKey = System.getenv("HANGAR_TOKEN")

        platforms {
            paper {
                jar = tasks.reobfJar.get().outputJar
                platformVersions = listOf("26.2")
            }
        }
    }
}
