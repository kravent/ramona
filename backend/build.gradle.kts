plugins {
    application
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.72"
}

group = "me.agaman.ramona"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    val ktorVersion = "1.3.2"
    val serializationVersion = "0.20.0"

    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":common"))

    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-html-builder:$ktorVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationVersion")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.jetbrains.exposed:exposed-core:0.24.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.24.1")
    implementation("com.h2database:h2:1.4.200")
    implementation("org.koin:koin-ktor:2.1.5")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.koin:koin-test:2.1.5")
    testImplementation("io.mockk:mockk:1.10.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    processResources {
        val tasksNeedingAssets = setOf("run", "assembleDist", "distTar", "distZip", "installDist")
        if (gradle.startParameter.taskNames.any { tasksNeedingAssets.contains(it) }) {
            dependsOn(":frontend:browserDevelopmentWebpack") // TODO use production build when needed
        }
    }
    test {
        useJUnitPlatform()
    }
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/main/kotlin")
kotlin.sourceSets["test"].kotlin.srcDirs("src/test/kotlin")

sourceSets["main"].resources.srcDirs("src/main/resources", "../frontend/build/distributions")
sourceSets["test"].resources.srcDirs("src/test/resources")

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}
