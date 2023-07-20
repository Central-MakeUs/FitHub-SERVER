package fithub.app.validation.validator;

import fithub.app.base.Code;
import fithub.app.repository.ArticleRepositories.ArticleRepository;
import fithub.app.validation.annotation.ExistArticle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class ArticleExistValidator implements ConstraintValidator<ExistArticle, Long> {

    private final ArticleRepository articleRepository;


    @Override
    public void initialize(ExistArticle constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if(!articleRepository.existsById(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(Code.ARTICLE_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
