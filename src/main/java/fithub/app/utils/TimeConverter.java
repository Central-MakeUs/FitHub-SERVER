package fithub.app.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class TimeConverter {

    public String convertTime(LocalDateTime time) {
        LocalDateTime currentTime = LocalDateTime.now();

        String result = null;
        Duration duration = Duration.between(time,currentTime);
        Long minutes = duration.toMinutes();
        Long hours = duration.toHours();
        Long days = duration.toDays();

        if (minutes < 1) {
            result = "방금";
        } else if (hours < 1) {
            result = minutes.toString() + "분 전";
        } else if (days < 1) {
            result = hours.toString() + "시간 전";
        } else {
            DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            String newFormattedTime = time.format(newFormatter);
            result = newFormattedTime;
        }
        return result;
    }
}
