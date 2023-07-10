package fithub.app.exception.handler;

import fithub.app.exception.common.CustomException;
import fithub.app.exception.common.ErrorCode;

public class PhoneAuthException extends CustomException {

    public PhoneAuthException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public PhoneAuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
