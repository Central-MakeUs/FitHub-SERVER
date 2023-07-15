package fithub.app.base.exception.handler;

import fithub.app.base.Code;
import fithub.app.base.exception.GeneralException;

public class PhoneAuthException extends GeneralException {

    public PhoneAuthException(Code errorCode) {
        super(errorCode);
    }
}
