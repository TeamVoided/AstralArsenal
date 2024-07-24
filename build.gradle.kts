@file:Suppress("PropertyName", "VariableNaming")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.iridium)
    alias(libs.plugins.iridium.publish)
    alias(libs.plugins.iridium.upload)
}

group = property("maven_group")!!
version = property("mod_version")!!
base.archivesName.set(modSettings.modId())

val modrinth_id: String? by project
val curse_id: String? by project

repositories {
    maven("https://maven.teamvoided.org/releases")
    maven("https://maven.terraformersmc.com/") { name = "Terraformers" }
    maven("https://maven.isxander.dev/releases") { name = "Xander Maven" }
    mavenCentral()
}

println("Task: " + gradle.startParameter.taskNames.joinToString(","))

modSettings {
    entrypoint("main", "org.teamvoided.astralarsenal.AstralArsenal::init")
    entrypoint("client", "org.teamvoided.astralarsenal.AstralArsenalClient::init")
    entrypoint("fabric-datagen", "org.teamvoided.astralarsenal.data.gen.AstralArsenalData")

    mixinFile("${modId()}.client.mixins.json")
    mixinFile("${modId()}.mixins.json")
    accessWidener("${modId()}.accesswidener")
}

dependencies {
    modImplementation(fileTree("libs"))
    modImplementation(libs.modmenu)
    modImplementation(libs.farrow)

    if (org.gradle.internal.os.OperatingSystem.current().isLinux) {
        modRuntimeOnly(libs.waygl)
    }
}

loom {
    splitEnvironmentSourceSets()
    runs {
        create("DataGen") {
            client()
            ideConfigGenerated(true)
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${file("src/main/generated")}")
            vmArg("-Dfabric-api.datagen.modid=${modSettings.modId()}")
            runDir("build/datagen")
        }

        create("TestWorld") {
            client()
            ideConfigGenerated(true)
            runDir("run")
            programArgs("--quickPlaySingleplayer", "test")
        }
    }
}

sourceSets["main"].resources.srcDir("src/main/generated")

tasks {
    val targetJavaVersion = 21
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(targetJavaVersion)
    }

    withType<KotlinCompile>().all {
        compilerOptions.jvmTarget = JvmTarget.JVM_21

        compilerOptions {
            freeCompilerArgs.add("-Xcontext-receivers")
        }
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(JavaVersion.toVersion(targetJavaVersion).toString()))
        withSourcesJar()
    }
    jar {
        val valTaskNames = gradle.startParameter.taskNames
        if (!valTaskNames.contains("runDataGen")) {
            exclude("org/teamvoided/astral_arsenal/data/gen/*")
        } else {
            println("Running datagen for task ${valTaskNames.joinToString(" ")}")
        }
    }
}

publishScript {
    releaseRepository("TeamVoided", "https://maven.teamvoided.org/releases")
    publication(modSettings.modId(), false)
    publishSources(true)
}

uploadConfig {
//    debugMode = true
    modrinthId = modrinth_id
    curseId = curse_id

    // FabricApi
    modrinthDependency("P7dR8mSH", uploadConfig.REQUIRED)
    curseDependency("fabric-api", uploadConfig.REQUIRED)
    // Fabric Language Kotlin
    modrinthDependency("Ha28R6CL", uploadConfig.REQUIRED)
    curseDependency("fabric-language-kotlin", uploadConfig.REQUIRED)
}