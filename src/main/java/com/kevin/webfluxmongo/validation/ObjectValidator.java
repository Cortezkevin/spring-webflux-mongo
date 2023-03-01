package com.kevin.webfluxmongo.validation;

import com.kevin.webfluxmongo.exception.ConstraintsFieldException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class ObjectValidator {

    private final Validator validator;

    @SneakyThrows
    public <T> T validate(T object){
        Set<ConstraintViolation<T>> errors = validator.validate(object);
        Map<String, Object> fieldErrors = new HashMap<>();
        if( errors.isEmpty() ){
            return object;
        }else {
            errors.forEach(e -> {
                fieldErrors.put(e.getPropertyPath().toString(), e.getMessage());
            });
            throw new ConstraintsFieldException(fieldErrors, HttpStatus.BAD_REQUEST, "The fields are not valid");
        }
    }

}