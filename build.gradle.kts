 plugins {
	java
	id("org.springframework.boot") version "3.5.7"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.bootcodeperu"
version = "0.0.1-SNAPSHOT"
description = "Demo project security-module for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	/* ===============================
     * SPRING BOOT STARTERS (CORE)
     * =============================== */
	implementation("org.springframework.boot:spring-boot-starter-web")
	// Spring Data JPA (Hibernate + repositorios JPA)
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	// Spring Data MongoDB (acceso a MongoDB)
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	// Spring Security (autenticación y autorización)
	implementation("org.springframework.boot:spring-boot-starter-security")
	// Actuator: métricas, health checks, endpoints de monitoreo
    implementation ("org.springframework.boot:spring-boot-starter-actuator")
	/* ===============================
   	 * BASES DE DATOS
   	 * =============================== */
	// Driver JDBC para PostgreSQL (solo en runtime)
	runtimeOnly("org.postgresql:postgresql")
	/* ===============================
     * SEGURIDAD - JWT
     * =============================== */
	// Necesario para la implementación de JWT (io.jsonwebtoken)
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly ("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly ("io.jsonwebtoken:jjwt-jackson:0.11.5")
	/* ===============================
     * MONITOREO Y MÉTRICAS
     * =============================== */
	// Micrometer/Prometheus: Formatea las métricas para que Prometheus las entienda
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	/* ===============================
     * MAPEO DE OBJETOS (DTO <-> ENTITY)
     * =============================== */
	// ModelMapper: mapeo automático entre objetos
	implementation("org.modelmapper:modelmapper:3.2.0")
	// MapStruct: mapeo en tiempo de compilación (más rápido y seguro)
	implementation("org.mapstruct:mapstruct:1.6.3")
	// Procesador de anotaciones de MapStruct
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
	// Evita conflictos entre Lombok y MapStruct
	implementation("org.projectlombok:lombok-mapstruct-binding:0.2.0")
	/* ===============================
     * LOMBOK
     * =============================== */
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	/* ===============================
     * CONFIGURACIÓN DE ENTORNO
     * =============================== */
    //implementation("me.paulschwarz:spring-dotenv:4.0.0")
	implementation("io.github.cdimascio:dotenv-java:3.1.0")

	/* ===============================
     * DESARROLLO
     * =============================== */
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	/* ===============================
     * TESTING
     * =============================== */
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
