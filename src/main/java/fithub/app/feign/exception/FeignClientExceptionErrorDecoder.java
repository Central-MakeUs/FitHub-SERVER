package fithub.app.feign.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import fithub.app.base.Code;
import fithub.app.base.exception.handler.CustomFeignClientException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class FeignClientExceptionErrorDecoder implements ErrorDecoder {

    Logger logger = LoggerFactory.getLogger(FeignClientExceptionErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() <= 499){
            logger.error("카카오 소셜 로그인 400번대 에러 발생 : {}", response.reason());
            return new CustomFeignClientException(Code.FEIGN_CLIENT_ERROR_400);
        }
        else{
            logger.error("카카오 소셜 로그인 500번대 에러 발생 : {}", response.reason());
            return new CustomFeignClientException(Code.FEIGN_CLIENT_ERROR_500);
        }
    }
}
