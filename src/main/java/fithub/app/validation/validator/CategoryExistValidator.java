package fithub.app.validation.validator;

import fithub.app.base.Code;
import fithub.app.domain.ExerciseCategory;
import fithub.app.repository.ExerciseCategoryRepository;
import fithub.app.validation.annotation.ExistCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class CategoryExistValidator implements ConstraintValidator<ExistCategory, Integer> {

    private final ExerciseCategoryRepository exerciseCategoryRepository;

    @Override
    public void initialize(ExistCategory constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        System.out.println("It works!");
        if(!exerciseCategoryRepository.existsById(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(Code.CATEGORY_ERROR.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
