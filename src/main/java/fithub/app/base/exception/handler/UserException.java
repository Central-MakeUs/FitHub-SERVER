package fithub.app.base.exception.handler;

import fithub.app.base.Code;
import fithub.app.base.exception.GeneralException;

public class UserException extends GeneralException {

    public UserException(Code errorCode) {
        super(errorCode);
    }
}
