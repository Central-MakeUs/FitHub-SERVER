package fithub.app.base.exception.handler;

import fithub.app.base.Code;
import fithub.app.base.exception.GeneralException;

public class AppleOAuthException extends GeneralException {

    public AppleOAuthException(Code errorCode) {
        super(errorCode);
    }
}
