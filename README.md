# Spring-cloud-contract
Poc demonstrando o uso de STUBS para validar contratos de comunicação entre um consumidor e um produtor.

## Produtor
build.gradle
```
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'org.springframework.cloud:spring-cloud-contract-gradle-plugin:2.1.3.RELEASE'
    }
}

plugins {
    id 'org.springframework.boot' version '2.1.8.RELEASE'
    id 'io.spring.dependency-management' version '1.0.8.RELEASE'
    id 'java'
}

apply plugin: 'spring-cloud-contract'
apply plugin: 'maven-publish'

contracts {
    baseClassForTests = 'br.com.produtor.BaseClass'
}

publish.dependsOn("publishStubsToScm")

group = 'br.com'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

ext {
    set('springCloudVersion', "Greenwich.SR3")
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.cloud:spring-cloud-starter-contract-stub-runner'
    testImplementation 'org.springframework.cloud:spring-cloud-starter-contract-verifier'

    testCompile('org.springframework.cloud:spring-cloud-contract-wiremock')
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}
```
