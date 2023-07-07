package fithub.app.exception.handler;

import fithub.app.exception.common.CustomException;
import fithub.app.exception.common.ErrorCode;

public class AppleOAuthException extends CustomException {
    public AppleOAuthException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public AppleOAuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
