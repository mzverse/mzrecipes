import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "mz"
version = "0.1"

val outputDir = File(rootProject.projectDir, "out")

repositories {
    mavenCentral()
    mavenLocal()
    gradlePluginPortal()
    var actionGithub: MavenArtifactRepository.() -> Unit = {
        credentials {
            username = if (System.getenv("CI") != null)
                System.getenv("GITHUB_ACTOR")
            else
                System.getenv("GITHUB_USERNAME")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
    maven("https://maven.pkg.github.com/mzverse/mzlib", actionGithub)
    maven("https://maven.fabricmc.net/")
    maven("https://libraries.minecraft.net/")
    maven("https://maven.aliyun.com/repository/public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.aliyun.com/repository/gradle-plugin/")
    maven("https://maven.aliyun.com/repository/apache-snapshots/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://raw.githubusercontent.com/TheBlackEntity/PlugMan/repository/")
    //    maven("https://maven.fastmirror.net/repositories/minecraft/")
    //    maven("https://oss.sonatype.org/content/repositories/snapshots")
    //    maven("https://repo.maven.apache.org/maven2/")
}

dependencies {
    compileOnly("org.mzverse:mzlib-minecraft:latest.integration")
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("net.fabricmc:fabric-loader:0.16.10")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    jar {
        archiveClassifier.set("original")
    }
    shadowJar {
        archiveClassifier.set("")
    }
    register<Copy>("copyBinaryResources") {
        from("src/main/resources") {
            include("**/*.js")
            include("**/*.png")
            include("lang/**/*")
            include("mappings/**/*")
        }
        into("build/resources/main")
    }
    processResources {
        dependsOn("copyBinaryResources")
        exclude("**/*.js")
        exclude("**/*.png")
        exclude("lang/**/*")
        exclude("mappings/**/*")
        expand("version" to project.version)
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    register<Copy>("moveJarToOutputDir") {
        val shadowJarTask = project.tasks.findByPath("shadowJar") as ShadowJar
        from(shadowJarTask.outputs.files)
        into(outputDir)
    }

    build {
        dependsOn(shadowJar)
        dependsOn("moveJarToOutputDir")
    }
}

tasks.test {
    useJUnitPlatform()
}