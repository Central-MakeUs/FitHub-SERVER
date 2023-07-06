package fithub.app.exception.handler;

import fithub.app.exception.common.ErrorCode;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(ErrorCode errorCode){
        super(errorCode.name());
    }
}
