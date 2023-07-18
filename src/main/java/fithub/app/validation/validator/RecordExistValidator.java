package fithub.app.validation.validator;

import fithub.app.base.Code;
import fithub.app.repository.RecordRepositories.RecordRepository;
import fithub.app.validation.annotation.ExistRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class RecordExistValidator implements ConstraintValidator<ExistRecord, Long> {

    private final RecordRepository recordRepository;

    @Override
    public void initialize(ExistRecord constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if(!recordRepository.existsById(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(Code.RECORD_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
