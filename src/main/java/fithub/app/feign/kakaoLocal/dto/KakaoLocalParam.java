package fithub.app.feign.kakaoLocal.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoLocalParam {

    private String query;
    private String x;
    private String y;
    private Integer page;
    private Integer radius;
}
