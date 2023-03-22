package com.kevin.webfluxmongo.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExceptionAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = new HashMap<>();
        Throwable throwable = super.getError(request);
        if( throwable instanceof CustomException ){
            CustomException customException = ( CustomException ) throwable;
            errorAttributes.put("status", customException.getStatus());
            errorAttributes.put("message", customException.getMessage());
        }
        if ( throwable instanceof ConstraintsFieldException ){
            ConstraintsFieldException constraintsFieldException = ( ConstraintsFieldException ) throwable;
            errorAttributes.put("fields", constraintsFieldException.getFieldsErrors());
            errorAttributes.put("status", constraintsFieldException.getStatus());
            errorAttributes.put("message", constraintsFieldException.getMessage());
        }
        if( throwable instanceof MalformedJwtException){
            MalformedJwtException malformedJwtException = ( MalformedJwtException ) throwable;
            errorAttributes.put("message", malformedJwtException.getMessage());
            errorAttributes.put("status", HttpStatus.BAD_REQUEST);
        }
        if( throwable instanceof SignatureException){
            SignatureException signatureException = ( SignatureException ) throwable;
            errorAttributes.put("message", "The token is invalid");
            errorAttributes.put("status", HttpStatus.BAD_REQUEST);
        }
        if( throwable instanceof MalformedJwtException){
            MalformedJwtException malformedJwtException = ( MalformedJwtException ) throwable;
            errorAttributes.put("message", "The token is malformed");
            errorAttributes.put("status", HttpStatus.BAD_REQUEST);
        }
        if( throwable instanceof ExpiredJwtException){
            ExpiredJwtException expiredJwtException = ( ExpiredJwtException ) throwable;
            errorAttributes.put("message", "The session is expired");
            errorAttributes.put("status", HttpStatus.BAD_REQUEST);
        }
        return errorAttributes;
    }
}
