package org.example.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumConstantValidator implements ConstraintValidator<EnumConstant, CharSequence> {

    private Set<String> allowed;

    @Override
    public void initialize(EnumConstant constraintAnnotation) {
        allowed = Arrays.stream(constraintAnnotation.allowed()).collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(CharSequence val, ConstraintValidatorContext constraintValidatorContext) {
        return val == null || val.isEmpty() || allowed.contains(String.valueOf(val));
    }
}
