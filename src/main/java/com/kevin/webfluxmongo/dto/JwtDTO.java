package com.kevin.webfluxmongo.dto;

import com.kevin.webfluxmongo.documents.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtDTO {
    private String token;
    private User user;
}
