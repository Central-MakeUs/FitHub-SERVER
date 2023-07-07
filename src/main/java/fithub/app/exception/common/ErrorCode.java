package fithub.app.exception.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    //Common
    FORBIDDEN(HttpStatus.FORBIDDEN, 4003, "접근 권한이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,4004 ,"인증정보가 유효하지 않습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,4005 ,"잘못된 요청 입니다."),

    // Auth
    JWT_BAD_REQUEST(HttpStatus.UNAUTHORIZED, 4006,"잘못된 JWT 서명입니다."),
    JWT_ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, 4007,"액세스 토큰이 만료되었습니다."),
    JWT_REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, 4008,"리프레시 토큰이 만료되었습니다. 다시 로그인하시기 바랍니다."),
    JWT_UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, 4009,"지원하지 않는 JWT 토큰입니다."),
    JWT_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, 4010,"유효한 JWT 토큰이 없습니다."),
    FAILED_TO_VALIDATE_APPLE_LOGIN(HttpStatus.UNAUTHORIZED, 4011, "애플 auth 서버에서 데이터를 받아오지 못했습니다"),
    FAILED_TO_FIND_AVALIABLE_RSA(HttpStatus.UNAUTHORIZED, 4012, "Identity Token에서 유효한 값을 찾지 못했습니다"),

    MEMBER_NOT_FOUND(HttpStatus.UNAUTHORIZED, 4013,"해당 사용자가 존재하지 않습니다");

    private final HttpStatus status;
    private final Integer code;
    private final String message;

    private Object result;



    ErrorCode(HttpStatus status, Integer code, String message){
        this.status = status;
        this.code = code;
        this.message = message;
        this.result = null;
    }

    public HttpStatus getStatus() {return status;}
    public Integer getCode() {
        return code;
    }
    public String getMessage() {return message;}

    public Object getResult() {return  result;}

}
