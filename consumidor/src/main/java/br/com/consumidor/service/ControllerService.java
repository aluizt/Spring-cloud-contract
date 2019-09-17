package br.com.consumidor.service;

import br.com.consumidor.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ControllerService {

    @Value("${endPoint_producer.domain}")
    private String domain;
    @Value("${endPoint_producer.port}")
    private int port;
    private RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity getResult(String endpoint) {

        final String address = domain + ":" + port + endpoint;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        if (endpoint.equals("/test")) {
            return restTemplate.exchange(
                    address,
                    HttpMethod.GET,
                    new HttpEntity<String>(headers),
                    String.class
            );
        } else {
            return restTemplate.getForEntity(address, User.class);
        }
    }
}
