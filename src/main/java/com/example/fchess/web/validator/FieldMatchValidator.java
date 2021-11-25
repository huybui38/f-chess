package com.example.fchess.web.validator;


import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String first;
    private String second;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        first = constraintAnnotation.first();
        second = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        try
        {
            final Object firstObj = PropertyAccessorFactory.forBeanPropertyAccess(o).getPropertyValue(first);
            final Object secondObj = PropertyAccessorFactory.forBeanPropertyAccess(o).getPropertyValue(second);
            return firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
        }
        catch (final Exception ignore)
        {
            // ignore
        }
        return true;
    }
}
