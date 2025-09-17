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

base.archivesName.set(modSettings.modId())

repositories {
    maven("https://teamvoided.org/releases") {
        content {
            includeGroup("org.teamvoided")
            includeGroup("net.wiredtomato")
        }
    }
    maven("https://teamvoided.org/snapshots") { content { includeGroup("org.teamvoided") } }
    maven("https://maven.fzzyhmstrs.me/") { name = "FzzyMaven"; content { includeGroup("me.fzzyhmstrs") } }
    maven("https://maven.terraformersmc.com/") {
        name = "Terraformers"
        content {
            includeGroup("com.terraformersmc")
            includeGroup("dev.emi")
        }
    }
    maven("https://api.modrinth.com/maven") { content { includeGroup("maven.modrinth") } }
    mavenCentral()
}

modSettings {
    entrypoint("main", "org.teamvoided.astralarsenal.AstralArsenal::init")
    entrypoint("client", "org.teamvoided.astralarsenal.AstralArsenalClient::init")
    entrypoint("fabric-datagen", "org.teamvoided.astralarsenal.data.gen.AstralArsenalData")

    mixinFile("${modId()}.client.mixins.json")
    mixinFile("${modId()}.mixins.json")
//    accessWidener("${modId()}.accesswidener")
}

dependencies {
    modImplementation(fileTree("libs"))
    modImplementation(libs.modmenu)

    modImplementation(libs.farrow)
    include(libs.farrow)

    modCompileOnly("${libs.emi.get()}:api")
    modLocalRuntime(libs.emi)

    // Testing
    //modImplementation(libs.creative.works)
    modCompileOnly(libs.imguimc)
}

val username = "vDev"
val uuid: String? = null

loom {
    splitEnvironmentSourceSets()
    runs {
        named("client") {
            programArgs("--username", username)
            uuid?.let { programArgs("--uuid", uuid) }
        }

        create("TestWorld") {
            client()
            ideConfigGenerated(true)
            runDir("run")
            programArgs("--quickPlaySingleplayer", "test", "--username", username)
            uuid?.let { programArgs("--uuid", uuid) }
        }

        create("DataGen") {
            client()
            ideConfigGenerated(true)
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${file("src/main/generated")}")
            vmArg("-Dfabric-api.datagen.modid=${modSettings.modId()}")
            runDir("build/datagen")
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
    modrinthId = "mt1fNBsN"
    curseId = "1073999"

    changeLog = File("changelog.md").readText()

// FabricApi
    modrinthDependency("P7dR8mSH", REQUIRED)
    curseDependency("fabric-api", REQUIRED)
// Fabric Language Kotlin
    modrinthDependency("Ha28R6CL", REQUIRED)
    curseDependency("fabric-language-kotlin", REQUIRED)
//Farrow
    modrinthDependency("uH6SVTfs", EMBEDDED)
}