package com.kevin.webfluxmongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginUserDTO {
    @NotBlank(message="Required")
    private String username;
    @NotBlank(message="Required")
    private String password;
}
