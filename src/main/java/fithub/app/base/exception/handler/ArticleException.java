package fithub.app.base.exception.handler;

import fithub.app.base.Code;
import fithub.app.base.exception.GeneralException;

public class ArticleException extends GeneralException {

    public ArticleException(Code errorCode) {
        super(errorCode);
    }
}
