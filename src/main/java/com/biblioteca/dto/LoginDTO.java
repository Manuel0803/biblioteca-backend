package com.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginDTO {
    private String email;
    private String password;
}