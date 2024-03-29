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
                new TestController() {
                    @Override
                    public ResponseEntity<User> getUser() {
                        var body = UserStub.getUser();
                        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
                    }
                }
        );
    }
}
