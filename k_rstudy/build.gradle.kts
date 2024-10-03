import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("jvm") version "1.9.24"
    kotlin("kapt") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.jpa") version "1.9.24"
    kotlin("plugin.allopen") version "1.9.24"
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.0")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

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
   // implementation("org.springframework.boot:spring-boot-starter-data-redis")

    //log4j
    //implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.4.11")  // logback 최신 버전 사용

    //pdf
    implementation("com.itextpdf:itextpdf:5.5.13.4")
    implementation ("com.lowagie:itext:2.1.7")

    //config 서버 안해줘도 업데이트 할수있음..
    implementation ("org.springframework.boot:spring-boot-starter-actuator")

    implementation ("org.projectlombok:lombok:1.18.26")
    testImplementation ("org.projectlombok:lombok:1.18.26")

    implementation ("org.apache.commons:commons-lang3:3.12.0")

    //카프카 추가
    implementation ("org.springframework.kafka:spring-kafka")

    implementation("io.github.resilience4j:resilience4j-spring-boot2:2.2.0")

    // Resilience4j Retry
    implementation("io.github.resilience4j:resilience4j-retry:2.2.0")

    // Resilience4j Bulkhead
    implementation("io.github.resilience4j:resilience4j-bulkhead:2.2.0")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.3") // Feign 의존성 추가

    //크롤링 메일발송처리
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.seleniumhq.selenium:selenium-java:4.10.0")


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