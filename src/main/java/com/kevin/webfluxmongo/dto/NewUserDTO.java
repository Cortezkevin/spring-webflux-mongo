package com.kevin.webfluxmongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewUserDTO {
    @Length(min = 5, max = 20, message = "Debe tener minimo 5 y maximo 20 caracteres")
    @NotBlank(message="Required")
    private String username;
    @NotBlank(message="Required")
    @Email(message = "Invalid email")
    private String email;
    @NotBlank(message="Required")
    private String password;
    private List<String> roles;
}
