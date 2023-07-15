package fithub.app.base.exception.handler;

import fithub.app.base.Code;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(Code code){
        super(code.name());
    }
}
