package fithub.app.converter;

import fithub.app.domain.HashTag;
import fithub.app.web.dto.responseDto.HashTagResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HashTagConverter {

    public static HashTagResponseDto.HashtagDto toHashtagDto(HashTag hashTag){
        return HashTagResponseDto.HashtagDto.builder()
                .hashTagId(hashTag.getId())
                .name(hashTag.getName())
                .build();
    }

    public static HashTagResponseDto.HashtagDtoList toHashtagDtoList(List<HashTag> hashTagList){
        List<HashTagResponseDto.HashtagDto> hashtagDtoList =
                hashTagList.stream()
                        .map(hashTag -> toHashtagDto(hashTag))
                        .collect(Collectors.toList());

        return HashTagResponseDto.HashtagDtoList.builder()
                .hashtags(hashtagDtoList)
                .size(hashtagDtoList.size())
                .build();
    }
}
