package fithub.app.service;

import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.web.dto.requestDto.RecordRequestDto;

import java.io.IOException;

public interface RecordService {

    public Record create(RecordRequestDto.CreateRecordDto request, User user, Integer categoryId) throws IOException;
}
