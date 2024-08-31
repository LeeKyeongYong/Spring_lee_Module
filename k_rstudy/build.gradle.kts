import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.Copy
import org.asciidoctor.gradle.AsciidoctorTask

plugins {
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    id("org.jetbrains.kotlin.jvm") version "1.9.24"
    id("org.jetbrains.kotlin.kapt") version "1.9.24"
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.24"
    id("org.asciidoctor.jvm.convert") version "3.3.1"
    kotlin("plugin.allopen") version "1.9.24"
}

group = "org.jc"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_19

sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
    kotlin.srcDir("$buildDir/generated/source/kapt/main")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    create("asciidoctorExt")
}

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin dependencies
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Spring Boot and related dependencies
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.testng:testng:7.1.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("mysql:mysql-connector-java:8.0.33")
    runtimeOnly("com.h2database:h2")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // Dev tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    // Security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    testImplementation("org.springframework.security:spring-security-test")

    // OAuth2
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")

    // Crawling library
    implementation("org.jsoup:jsoup:1.17.2")

    // Excel library
    implementation("org.apache.poi:poi-ooxml:5.2.5")
    implementation("org.apache.poi:poi:5.2.5")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")

    // Spring AOP
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // Web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")

    // Jasypt
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")
    implementation("org.jasypt:jasypt:1.9.3")

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")

    // Batch
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.batch:spring-batch-integration")
    testRuntimeOnly("org.springframework.batch:spring-batch-test")

    // RestDocs
    implementation("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
}

val snippetsDir = file("build/generated-snippets")

tasks.named<Test>("test") {
    useJUnitPlatform()
    outputs.dir(snippetsDir)
}

tasks.register<AsciidoctorTask>("asciidoctor") {
    configurations("asciidoctorExt")
    baseDirFollowsSourceFile()
    inputs.dir(snippetsDir)
    dependsOn(tasks.named("test"))
}

tasks.named<AsciidoctorTask>("asciidoctor") {
    doFirst {
        delete(file("src/main/resources/static/docs"))
    }
}

tasks.register<Copy>("copyDocument") {
    dependsOn(tasks.named("asciidoctor"))
    from(file("build/docs/asciidoc"))
    into(file("src/main/resources/static/docs"))
}

tasks.named("build") {
    dependsOn(tasks.named("copyDocument"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "19"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}