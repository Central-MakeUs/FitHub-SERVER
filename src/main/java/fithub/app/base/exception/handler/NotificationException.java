package fithub.app.base.exception.handler;

import fithub.app.base.Code;
import fithub.app.base.exception.GeneralException;

public class NotificationException extends GeneralException {

    public NotificationException(Code errorCode) {
        super(errorCode);
    }
}
