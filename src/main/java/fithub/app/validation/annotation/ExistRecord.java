package fithub.app.validation.annotation;

import fithub.app.validation.validator.CategoryExistValidator;
import fithub.app.validation.validator.RecordExistValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RecordExistValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistRecord {
}
