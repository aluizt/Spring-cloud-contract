package br.com.produtor.controller;

import br.com.produtor.model.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping()
    public ResponseEntity<String> get(){
        return ResponseEntity.ok("OK");
    }

    @GetMapping(value="/user")
    public ResponseEntity<User>  getUser(){
        var user = User.builder().name("Janice").registration("12345").credit(true).build();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(user);
    }
}
