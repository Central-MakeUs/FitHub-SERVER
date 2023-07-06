package fithub.app.utils;

import org.springframework.http.HttpStatus;

public enum ResponseCode {

    AUTO_LOGIN_NEW_FACE(HttpStatus.OK, "2001", "새로온 사용자 입니다."),
    AUTO_LOGIN_INFO_NULL(HttpStatus.OK, "2002", "회원정보 입력이 필요합니다."),
    AUTO_LOGIN_SUCCESS(HttpStatus.OK, "2001", "로그인 된 사용자 입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;



    ResponseCode(HttpStatus status, String code, String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {return status;}
    public String getCode() {
        return code;
    }
    public String getMessage() {return message;}
}
