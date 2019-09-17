# Spring-cloud-contract
Poc demonstrando o uso de STUBS para validar contratos de comunicação entre um consumidor e um produtor.

## Produtor

##### Contrato
No Spring Cloud Contract, um contrato pode ser definido em um arquivo Groovy, YAML ou Pact. 
Para este exemplo estaremos utilizando um arquivo YAML:

```
description: |
  Represents a scenario of sending request to /test
request:
  method: GET
  url: /test/user
response:
  status: 200
  body:
    name: Joao da Silva
    registration: 12345
    credit: true
  headers:
    contentType: application/json
```
Neste exemplo temos                                                 
Description : com uma descrição do senario que sera testado.                               
Request     : com o metodo e a url que sera testada.                                      
Response    : contem a resposta que esperamos, neste exemplo queremos o status 200 com um body contendo os atributos de um objeto, alem de um headers com o contentType.   

#### O Controller

```
package br.com.produtor.controller;

import br.com.produtor.model.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping(value="/user")
    public ResponseEntity<User>  getUser(){
        var user = User.builder().name("Paulo").registration("12345").credit(true).build();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(user);
    }
}
```

#### Classe Basse

Para que o plugin possa criar os testes é necessário uma classe base no pacote de testes que sera utilizada como exemplo para a criação dos testes.

```
package br.com.produtor;

import br.com.produtor.controller.TestController;
import br.com.produtor.model.User;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class BaseClass {

    @Before
    public void setup() {
            RestAssuredMockMvc.standaloneSetup(
                new TestController(){
                    @Override
                    public ResponseEntity<User>  getUser(){
                        var body = UserStub.getUser();
                        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
                    }
                }
        );
    }
}
```

#### A classe de teste criada
```
package br.com.produtor;

import br.com.produtor.BaseClass;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.response.ResponseOptions;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions.assertThat;
import static org.springframework.cloud.contract.verifier.util.ContractVerifierUtil.*;

public class ContractVerifierTest extends BaseClass {

	@Test
	public void validate_controller2() throws Exception {
		// given:
			MockMvcRequestSpecification request = given();

		// when:
			ResponseOptions response = given().spec(request)
					.get("/test/user");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['name']").isEqualTo("Joao da Silva");
			assertThatJson(parsedJson).field("['registration']").isEqualTo(12345);
			assertThatJson(parsedJson).field("['credit']").isEqualTo(true);
	}

}
```

#### build.gradle
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
