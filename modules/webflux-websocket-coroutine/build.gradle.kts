import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version app.boboc.Deps.springBootVersion
    id("io.spring.dependency-management") version "1.1.4"
    signing
    id("com.gradleup.nmcp").version("0.0.7")
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    `maven-publish`
}



val libVersion = app.boboc.Deps.websocketCoroutineVersion
val artifactName = "webflux-websocket-coroutine"
val groupName = app.boboc.Deps.groupName

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootBuildImage {
    builder.set("paketobuildpacks/builder-jammy-base:latest")
}


val javadocJar = tasks.register("javadocJar", Jar::class) {
    archiveClassifier = "javadoc"
    sourceSets["main"].allSource
    group = "deploy"
}

val sourcesJar = tasks.register("sourcesJar", Jar::class) {
    archiveClassifier = "sources"
    sourceSets["main"].allSource
    group = "deploy"
}

val deployJar = tasks.register("deployJar", Jar::class) {
    archiveClassifier = "sources"
    sourceSets["main"].allSource
    group = "deploy"
}

tasks.jar{
    archiveClassifier = ""
}



val publishing = project.extensions.getByType(PublishingExtension::class)

publishing.publications {
    create<MavenPublication>("webSocket") {
        groupId = groupName
        artifactId = artifactName
        version = libVersion

        from(project.components["java"])

        artifact(tasks.kotlinSourcesJar)
        artifact(javadocJar)

        pom {
            name = "webflux-websocket-coroutine"
            description = "Webflux WebSocket Coroutine"
            url = "https://github.com/boboc-app/webflux-websocket-coroutine"
            issueManagement {
                url = "https://github.com/boboc-app/webflux-websocket-coroutine/issues"
            }
            developers {
                developer {
                    id = "bo.kang"
                    email = "ebfks0301@gmail.com"
                    name = "Bo Chan Kang"
                }
            }
            scm {
                url = "https://github.com/boboc-app/webflux-websocket-coroutine"
                connection = "scm:git:github.com/boboc-app/webflux-websocket-coroutine.git"
            }
            licenses {
                license {
                    name = "MIT License"
                    url = "https://github.com/boboc-app/webflux-websocket-coroutine/blob/main/LICENSE"
                }
            }


        }

    }

    project.extensions.getByType(SigningExtension::class.java).apply {
        useGpgCmd()
        sign(publishing.publications)
    }
}

nmcp {
    publish("webSocket") {
        username = System.getenv("MAVEN_CENTRAL_USERNAME")
        password = System.getenv("MAVEN_CENTRAL_PASSWORD")
        publicationType = "USER_MANAGED"
        version = libVersion
    }
}
