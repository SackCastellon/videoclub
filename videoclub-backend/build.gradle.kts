import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version kotlinVersion
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(ktor("server-netty"))
    implementation(ktor("auth-jwt"))
    implementation(ktor("jackson"))
    implementation(ktor("websockets"))
    implementation(ktor("network-tls"))
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf(
                "-Xjsr305=strict",
                "-Xuse-experimental=io.ktor.util.KtorExperimentalAPI"
            )
        }
    }

    val generateJks by register<JavaExec>("generateJks") {
        classpath = sourceSets["main"].runtimeClasspath
        main = "videoclub.util.CertificateGenerator"
    }

    getTasksByName("run", false).first().dependsOn(generateJks)
}
