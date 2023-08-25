package fithub.app.converter;

import fithub.app.domain.HashTag;
import fithub.app.domain.mapping.ArticleHashTag;
import fithub.app.domain.mapping.RecordHashTag;
import fithub.app.repository.HashTagRepositories.HashTagRepository;
import fithub.app.web.dto.responseDto.HashTagResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HashTagConverter {

    private final HashTagRepository hashTagRepository;

    private static HashTagRepository staticHashTagRepository;

    @PostConstruct
    public void init() {
        staticHashTagRepository = this.hashTagRepository;
    }

    public static HashTag newHashTag(String tag){
        HashTag newTag = HashTag.builder().name(tag).articleHashTagList(new ArrayList<>()).recordHashTagList(new ArrayList<>()).
                build();
        HashTag savedHashTag = staticHashTagRepository.save(newTag);
        return savedHashTag;
    }

    public static HashTagResponseDto.HashtagDto toHashtagDto(HashTag hashTag){
        return HashTagResponseDto.HashtagDto.builder()
                .hashTagId(hashTag.getId())
                .name(hashTag.getName())
                .build();
    }

    public static HashTagResponseDto.HashtagDtoList toHashtagDtoList(List<ArticleHashTag> hashTagList, HashTag exerciseHashTag){
        List<HashTagResponseDto.HashtagDto> hashtagDtoList =
                hashTagList.stream()
                        .map(hashTag -> toHashtagDto(hashTag.getHashTag()))
                        .collect(Collectors.toList());

        HashTagResponseDto.HashtagDto hashtagDto = toHashtagDto(exerciseHashTag);
        hashtagDtoList.add(0,hashtagDto);

        return HashTagResponseDto.HashtagDtoList.builder()
                .hashtags(hashtagDtoList)
                .size(hashtagDtoList.size())
                .build();
    }

    public static HashTagResponseDto.HashtagDtoList toHashtagDtoListRecord(List<RecordHashTag> hashTagList, HashTag exerciseTag){
        List<HashTagResponseDto.HashtagDto> hashtagDtoList =
                hashTagList.stream()
                        .map(hashTag -> toHashtagDto(hashTag.getHashTag()))
                        .collect(Collectors.toList());

        HashTagResponseDto.HashtagDto exerciseHashTagDto = toHashtagDto(exerciseTag);
        hashtagDtoList.add(0,exerciseHashTagDto);

        return HashTagResponseDto.HashtagDtoList.builder()
                .hashtags(hashtagDtoList)
                .size(hashtagDtoList.size())
                .build();
    }
}
