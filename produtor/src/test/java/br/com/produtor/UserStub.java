package br.com.produtor;

import br.com.produtor.model.User;

public class UserStub {

    public static User getUser(){
        return User.builder().name("Alexandre tavares stub").registration("12345").credit(true).build();
    }
}
