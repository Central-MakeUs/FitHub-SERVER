package fithub.app.feign.kakaoLocal;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import fithub.app.feign.exception.FeignClientExceptionErrorDecoder;
import org.springframework.context.annotation.Bean;

public class KakaoFeignConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor(){
        return template -> template.header("Content-Type", "application/json;charset=UTF-8");
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return  new FeignClientExceptionErrorDecoder();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
