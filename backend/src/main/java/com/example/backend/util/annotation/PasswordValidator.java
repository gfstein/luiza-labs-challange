package com.example.backend.util.annotation;

import com.example.backend.util.Validator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class PasswordValidator implements ConstraintValidator<ValidPassword, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof String password) {
            return Validator.isValidPassword(password);
        } else if (value instanceof Optional<?> optional) {
            return optional.map(val -> Validator.isValidPassword((String) val)).orElse(true);
        }
        return false;
    }
}
