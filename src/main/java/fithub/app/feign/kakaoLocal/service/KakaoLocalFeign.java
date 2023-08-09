package fithub.app.feign.kakaoLocal.service;

import fithub.app.feign.kakaoLocal.KakaoFeignConfiguration;
import fithub.app.feign.kakaoLocal.dto.KakaoLocalParam;
import fithub.app.feign.kakaoLocal.dto.KakaoLocalResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "KakaoLocal", url = "https://dapi.kakao.com", configuration = KakaoFeignConfiguration.class)
@Component
public interface KakaoLocalFeign {
    @GetMapping("/v2/local/search/keyword")
    KakaoLocalResponseDto getLocalInfo(@RequestHeader(name = "Authorization") String Authorization, @SpringQueryMap KakaoLocalParam kakaoLocalParam);
}
