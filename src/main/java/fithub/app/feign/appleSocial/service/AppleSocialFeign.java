package fithub.app.feign.appleSocial.service;

import fithub.app.feign.appleSocial.AppleSocialFeignConfiguration;
import fithub.app.feign.appleSocial.dto.ApplePublicKeyListDTO;
import fithub.app.feign.kakaoLocal.KakaoFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "apple-public-key-client", url = "https://appleid.apple.com/auth",configuration = AppleSocialFeignConfiguration.class)
@Component
public interface AppleSocialFeign {
    @GetMapping("/keys")
    ApplePublicKeyListDTO getApplePublicKeys();
}
