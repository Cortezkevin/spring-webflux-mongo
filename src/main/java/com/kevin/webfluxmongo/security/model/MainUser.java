package com.kevin.webfluxmongo.security.model;
import com.kevin.webfluxmongo.documents.User;
import com.kevin.webfluxmongo.security.enums.RolName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
public class MainUser implements UserDetails {

    private String username;
    @Getter
    private String email;
    private String password;
    @Getter @Setter
    private List<RolName> roles;

    public static MainUser build(User user){
        /*List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(rol -> rol.name())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());*/
        return new MainUser(user.getUsername(), user.getEmail(), user.getPassword(), user.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map( r -> r.name()).map( SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
