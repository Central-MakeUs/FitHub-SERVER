package fithub.app.web.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

public class RecordRequestDto {

    @Getter
    @Setter
    public static class uploadRecordDto{
        Long categoryId;
        String title;
        String contents;
    }

    @Getter @Setter
    public static class updateRecordDto{
        String title;
        String contents;
    }

    @Getter @Setter
    public static class deleteListRecordDto{
        Long[] recordIdList;
    }
}
