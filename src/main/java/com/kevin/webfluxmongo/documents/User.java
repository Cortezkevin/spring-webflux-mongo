package com.kevin.webfluxmongo.documents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kevin.webfluxmongo.security.enums.RolName;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;;

@Document("users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class User {

    @Id
    private String id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private List<RolName> roles = new ArrayList<>();

}
