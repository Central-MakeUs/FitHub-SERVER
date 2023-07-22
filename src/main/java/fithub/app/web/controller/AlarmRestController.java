package fithub.app.web.controller;

import fithub.app.firebase.service.FireBaseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "알람 API", description = "푸쉬 알람 API, 임시!!!!!!")
@RestController
@RequiredArgsConstructor
public class AlarmRestController {

    private final FireBaseService fireBaseService;

    @PostMapping("/alarm")
    public String pushMessage( )throws IOException {
        return null;
    }
}
