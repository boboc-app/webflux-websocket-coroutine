plugins {
    id("org.springframework.boot") version app.boboc.Deps.springBootVersion
    id("io.spring.dependency-management") version "1.1.4"
    `maven-publish`
    signing
    id("com.gradleup.nmcp").version("0.0.7")
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
}

val libVersion = app.boboc.Deps.websocketCoroutineVersion
val artifactName = "webflux-websocket-coroutine-extension"
val groupName = app.boboc.Deps.groupName

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    api(project(":modules:webflux-websocket-coroutine"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
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

project.dependencyManagement{
    dependencies {
        dependency("org.springframework.boot:spring-boot-starter-webflux:${app.boboc.Deps.springBootVersion}")
        dependency("app.boboc:webflux-websocket-coroutine:${libVersion}")
    }
}

val publishing = project.extensions.getByType(PublishingExtension::class)


publishing.publications {
    create<MavenPublication>("webSocketExt") {
        from(project.components["java"])
        artifact(tasks.kotlinSourcesJar)
        artifact(javadocJar)
        groupId = groupName
        artifactId = artifactName
        version = libVersion
        pom {
            name = "webflux-websocket-coroutine-extension"
            description = "Webflux WebSocket Coroutine Extension"
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
    publish("webSocketExt") {
        username = System.getenv("MAVEN_CENTRAL_USERNAME")
        password = System.getenv("MAVEN_CENTRAL_PASSWORD")
        publicationType = "USER_MANAGED"
        version = libVersion
    }
}

