package fithub.app.base.exception.handler;

import fithub.app.base.Code;
import fithub.app.base.exception.GeneralException;

public class CommentsException extends GeneralException {
    public CommentsException(Code errorCode) {
        super(errorCode);
    }
}
