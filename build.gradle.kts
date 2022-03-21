/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn how to create Gradle builds at https://guides.gradle.org/creating-new-gradle-builds
 */
plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.3.4"
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("io.freefair.lombok") version "6.1.0"
}

group = "me.lofro"
version = "1.0-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}




repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.codemc.io/repository/nms/")
    maven("https://libraries.minecraft.net/")
    maven("https://plugins.gradle.org/m2")
    maven("https://repo.aikar.co/content/groups/aikar/")
}


dependencies {
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")

    compileOnly("com.comphenix.protocol:ProtocolLib:4.7.0")

    implementation("net.kyori:adventure-text-minimessage:4.10.1")

	compileOnly("org.projectlombok:lombok:1.18.22")
	annotationProcessor("org.projectlombok:lombok:1.18.22")
	testCompileOnly("org.projectlombok:lombok:1.18.22")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.22")

    implementation("co.aikar:acf-paper:0.5.0-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.compilerArgs.addAll(listOf("-parameters"))
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
       
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }

    shadowJar {
        relocate("co.aikar.commands", "shadded.acf")
        relocate("co.aikar.locales", "shadded.acf.locales")
        relocate("acf-minecraft_", "shadded.acf.properties.acf-minecraft_")
        relocate("acf-core_", "shadded.acf.properties.acf-core_")
    }
}

bukkit {
    name = "SquidOtakuGame"
    version = "1.0"
    apiVersion = "1.18"
    main = "me.lofro.core.paper.Main"
    author = "Lofro"
}
