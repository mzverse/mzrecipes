plugins {
    id("java")
}

group = "mz"
version = "0.1"

repositories {
    mavenCentral()
    mavenLocal()
    gradlePluginPortal()
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
    compileOnly(files("D:/projects/MzLib/out/MzLibMinecraft-10.0.1-beta-dev16.jar"))
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("net.fabricmc:fabric-loader:0.16.10")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}