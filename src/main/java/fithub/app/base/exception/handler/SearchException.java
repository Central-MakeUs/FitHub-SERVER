package fithub.app.base.exception.handler;

import fithub.app.base.Code;
import fithub.app.base.exception.GeneralException;

public class SearchException extends GeneralException {
    public SearchException(Code errorCode) {
        super(errorCode);
    }
}
