package com.kevin.webfluxmongo.security.jwt;

import com.kevin.webfluxmongo.security.model.MainUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    private Key key;

    @PostConstruct
    private void init(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(MainUser mainUser){
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", mainUser.getRoles());
        return Jwts.builder()
                .setSubject( mainUser.getUsername() )
                .setClaims(claims)
                .setIssuedAt( new Date())
                .setExpiration( new Date( new Date().getTime() + (expiration * 1000 )))
                .signWith(key)
                .compact();

    }

    public Claims getAllClaimsFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private boolean isExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String getUserNameFromToken(String token){
        return getAllClaimsFromToken(token).getSubject();
    }

    public boolean validateToken(String token){
        return !isExpired(token);
    }
}
