package fithub.app.utils;

import org.springframework.http.HttpStatus;

public enum ResponseCode {

    SUCCESS(2000, "성공", null),

    AUTO_LOGIN_NEW_FACE(2003, "새로온 사용자 입니다.", null),
    AUTO_LOGIN_INFO_NULL(2002, "회원정보 입력이 필요합니다.", null),
    AUTO_LOGIN_SUCCESS(2001, "로그인 된 사용자 입니다.", null),

    KAKAO_OAUTH_LOGIN(2004, "로그인 입니다.", null),

    KAKAO_OAUTH_JOIN(2005, "회원가입 입니다.",null),

    APPLE_OAUTH_LOGIN(2006, "로그인 입니다.", null),

    APPLE_OAUTH_JOIN(2007, "회원가입 입니다.", null),

    NICKNAME_EXIST(2010, "닉네임이 이미 존재합니다.",null),

    NICKNAME_OK(2011, "사용 가능한 닉네임 입니다.", null);

    private final Integer code;
    private final String message;
    private final Object result;



    ResponseCode(Integer code, String message, Object result){
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public Integer getCode() {
        return code;
    }
    public String getMessage() {return message;}

    public Object getResult() {return result;}
}
