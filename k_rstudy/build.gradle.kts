import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    id("org.jetbrains.kotlin.jvm") version "1.9.24"
    id("org.jetbrains.kotlin.kapt") version "1.9.24"
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.24"

    val kotlinVersion = "1.9.24"
    kotlin("plugin.allopen") version kotlinVersion
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
}

repositories {
    mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")

dependencies {
    // Kotlin 의존성
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Spring Boot 및 관련 의존성
    implementation("org.springframework.boot:spring-boot-starter")

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.testng:testng:7.1.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // DB
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("mysql:mysql-connector-java:8.0.33")
    runtimeOnly("com.h2database:h2")

    // LOMBOK
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    kapt("org.projectlombok:lombok") // Kotlin의 경우

    // DEV TOOLS
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

    // SWAGGER
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    // 시큐리티
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    testImplementation("org.springframework.security:spring-security-test")

    // OAUTH2
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    // crawling lib
    implementation("org.jsoup:jsoup:1.17.2")

    // excel lib
    implementation("org.apache.poi:poi-ooxml:5.3.0")
    implementation("org.apache.poi:poi:5.3.0")

    //코루틴
    implementation("com.github.ulisesbocchio:jasypt-spring-boot:3.0.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")

    // Spring AOP 의존성 추가
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // WEB
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")

    //처리
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")

    implementation("org.jasypt:jasypt:1.9.3") // Jasypt 라이브러리
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2") // JUnit 5 API
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2") // JUnit 5 엔진

    //배치
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.batch:spring-batch-integration")
    testRuntimeOnly("org.springframework.batch:spring-batch-test")

    //웹소켓
    implementation ("org.springframework.boot:spring-boot-starter-websocket")
    implementation ("org.awaitility:awaitility")

    //jwt
    implementation ("io.jsonwebtoken:jjwt:0.12.3")
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")

    //qrcode
    implementation ("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation ("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.google.zxing:core:3.5.0")
    implementation("com.google.zxing:javase:3.5.0")

    // redis - LocalDateTime
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    //log4j
    implementation("ch.qos.logback:logback-classic:1.4.11")  // logback 최신 버전 사용

    //pdf
    implementation("com.itextpdf:itextpdf:5.5.13.4")

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