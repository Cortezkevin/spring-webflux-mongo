package com.kevin.webfluxmongo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserDTO {
    @Length(min = 5, max = 20, message = "Debe tener minimo 5 y maximo 20 caracteres")
    private String username;
    @Email(message = "Invalid email")
    private String email;
    private String password;
    private List<String> roles;
}
