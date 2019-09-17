package br.com.consumidor.controller;


import br.com.consumidor.model.User;
import br.com.consumidor.service.ControllerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/test")
public class TestController {

    private ControllerService controllerService;

    @GetMapping()
    public HttpStatus get() {
        final ResponseEntity result = controllerService.getResult("/test");
        return result.getStatusCode();
    }

    @GetMapping(path = "/user")
    public ResponseEntity<User> getUser() {
        final ResponseEntity<User> result = controllerService.getResult("/test/user");
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(result.getBody());
    }
}