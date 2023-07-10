package fithub.app.web.dto.requestDto;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ExerciseCategoryRequestDto {

    @Getter @Setter
    public static class SelectedCategoryListDto{
        List<Integer> categoryList;
    }
}
