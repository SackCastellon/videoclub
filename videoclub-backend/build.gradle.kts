import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version kotlinVersion
    id("com.github.johnrengelman.shadow") version "5.2.0"
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
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation(koin("ktor"))
    implementation(koin("logger-slf4j"))

    implementation(exposed("core"))
    implementation(exposed("jdbc"))
    implementation(exposed("java-time"))

    implementation("com.zaxxer:HikariCP:3.4.1")
    implementation("org.postgresql:postgresql:42.2.8")

    implementation("org.springframework.security:spring-security-crypto:5.2.1.RELEASE")
    implementation("org.springframework:spring-jcl:5.2.1.RELEASE")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.64")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf(
                "-Xjsr305=strict",
                "-Xuse-experimental=kotlin.time.ExperimentalTime",
                "-Xuse-experimental=io.ktor.util.KtorExperimentalAPI"
            )
        }
    }

    shadowJar.configure {
        baseName = "videoclub"
        classifier = null
        version = null
    }
}
