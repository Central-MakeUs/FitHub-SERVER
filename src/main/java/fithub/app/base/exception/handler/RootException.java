package fithub.app.base.exception.handler;

import fithub.app.base.Code;
import fithub.app.base.exception.GeneralException;

public class RootException extends GeneralException {
    public RootException(Code errorCode) {
        super(errorCode);
    }
}
