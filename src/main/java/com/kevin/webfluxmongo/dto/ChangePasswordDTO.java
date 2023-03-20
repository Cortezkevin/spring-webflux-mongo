package com.kevin.webfluxmongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordDTO {
    @NotBlank( message = "Required")
    private String tokenPassword;

    @NotBlank( message = "Required")
    private String password;

    @NotBlank( message = "Required")
    private String confirmPassword;
}
