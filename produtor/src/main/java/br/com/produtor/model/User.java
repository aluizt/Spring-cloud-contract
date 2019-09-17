package br.com.produtor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class User {
    private String registration;
    private String name;
    private Boolean credit;
}
