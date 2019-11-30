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

    implementation(koin("ktor"))
    implementation(koin("logger-slf4j"))

    implementation(exposed("core"))
    implementation(exposed("jdbc"))
    implementation(exposed("java-time"))
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
