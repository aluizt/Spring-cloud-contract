package br.com.consumidor.controller;

import br.com.consumidor.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureStubRunner(
        ids = "br.com:produtor:+:stubs:6565",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)

public class TestControllerTest {


    @Autowired
    private MockMvc mvc;

    private RestTemplate restTemplate = new RestTemplate();


    @Test
    public void when_get_then_return_200_Ok() throws Exception {

        var result = getResult("/test");
        Assert.assertTrue(result.contains("200,OK"));
    }

    @Test
    public void when() throws Exception {

        var result = getResult("/test/user");

        Assert.assertTrue(result.contains("Alexandre tavares stub"));
        Assert.assertTrue(result.contains("12345"));
        Assert.assertTrue(result.contains("true"));

    }

    private String getResult(String endpoint) {

        final String address = "http://localhost:6565/" + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        ResponseEntity<String> response = restTemplate.exchange(
                address,
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                String.class
        );

        return response.toString();
    }

}

























