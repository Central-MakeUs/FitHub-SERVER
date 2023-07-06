package fithub.app.exception.handler;

import fithub.app.exception.common.CustomException;
import fithub.app.exception.common.ErrorCode;

public class UserException extends CustomException {
    public UserException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
