package fithub.app.base.exception.handler;

import fithub.app.base.Code;
import fithub.app.base.exception.GeneralException;

public class RecordException extends GeneralException {
    public RecordException(Code errorCode) {
        super(errorCode);
    }
}
