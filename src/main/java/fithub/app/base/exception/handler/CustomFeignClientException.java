package fithub.app.base.exception.handler;

import fithub.app.base.Code;
import fithub.app.base.exception.GeneralException;

public class CustomFeignClientException extends GeneralException {

    public CustomFeignClientException(Code errorCode){
        super(errorCode);
    }
}
