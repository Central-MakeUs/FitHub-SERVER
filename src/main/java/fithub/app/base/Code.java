package fithub.app.base;

import com.google.api.Http;
import fithub.app.base.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum Code {

    OK(HttpStatus.OK,2000, "Ok"),

    KAKAO_OAUTH_LOGIN(HttpStatus.OK,2004, "로그인 입니다."),

    KAKAO_OAUTH_JOIN(HttpStatus.OK,2005, "회원가입 입니다."),

    APPLE_OAUTH_LOGIN(HttpStatus.OK,2006, "로그인 입니다."),

    APPLE_OAUTH_JOIN(HttpStatus.OK,2007, "회원가입 입니다."),

    AUTO_LOGIN_MAIN(HttpStatus.OK, 2008, "메인 화면으로 이동해도 됩니다."),

    AUTO_LOGIN_NOT_MAIN(HttpStatus.OK, 2009, "로그인 화면으로 이동해주세요"),

    NICKNAME_EXIST(HttpStatus.OK,2010, "닉네임이 이미 존재합니다."),

    NICKNAME_OK(HttpStatus.OK,2011, "사용 가능한 닉네임 입니다."),

    SEARCH_NO_DATA(HttpStatus.OK, 2021, "검색 결과가 없습니다."),

    PASSWORD_CORRECT(HttpStatus.OK, 2022, "비밀번호가 일치합니다"),
    PASSWORD_INCORRECT(HttpStatus.OK, 2023, "비밀번호가 일치하지 않습니다"),
    // Error Code
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5000, "Internal server Error"),

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

    MEMBER_NOT_FOUND(HttpStatus.UNAUTHORIZED, 4013,"해당 사용자가 존재하지 않습니다"),

    PHONE_AUTH_ERROR(HttpStatus.UNAUTHORIZED, 4014, "인증 번호가 맞지 않습니다."),

    PHONE_AUTH_TIMEOUT(HttpStatus.UNAUTHORIZED, 4015, "유효시간이 지났습니다."),

    PHONE_AUTH_NOT_FOUND(HttpStatus.BAD_REQUEST, 4016, "인증문자 발급이 필요합니다."),

    NO_EXERCISE_CATEGORY_EXIST(HttpStatus.BAD_REQUEST, 4017, "선호하는 운동 카테고리가 잘못 되었습니다."),


    EXIST_PHONE_USER(HttpStatus.BAD_REQUEST,4018, "이미 가입 된 회원입니다."),

    NO_PHONE_USER(HttpStatus.BAD_REQUEST,4019, "해당 계정이 없습니다."),

    PASSWORD_ERROR(HttpStatus.BAD_REQUEST,4020, "비밀번호가 틀렸습니다."),

    // category error
    CATEGORY_ERROR(HttpStatus.BAD_REQUEST,4030, "카테고리가 잘못 되었습니다."),

    // article error
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, 4031, "게시글이 존재하지 않습니다."),
    ARTICLE_FORBIDDEN(HttpStatus.FORBIDDEN, 4032, "다른 사람의 게시글"),

    // record error
    RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, 4041, "운동 인증이 존재하지 않습니다"),
    RECORD_FORBIDDEN(HttpStatus.FORBIDDEN, 4042, "다른 사람의 운동 인증"),

    //comments error
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, 4051, "댓글이 존재하지 않습니다."),
    COMMENTS_FORBIDDEN(HttpStatus.FORBIDDEN, 4052, "다른 사람의 댓글 입니다."),
    COMMENTS_BAD_REQUEST(HttpStatus.BAD_REQUEST, 4053, "url에 type을 확인해주세요."),
    COMMENTS_LIKES_FORBBIDDEN(HttpStatus.FORBIDDEN, 4054, "자신의 댓글은 좋아요를 누를 수 없습니다."),

    //report error
    ALREADY_REPORT(HttpStatus.BAD_REQUEST, 4061, "이미 신고 했습니다."),
    MY_CONTENTS(HttpStatus.BAD_REQUEST, 4062, "자신의 콘텐츠는 신고가 불가능합니다."),
    SELF_REPORT(HttpStatus.BAD_REQUEST, 4063, "스스로를 신고할 수 없습니다."),
    BLOCKED_USER(HttpStatus.FORBIDDEN, 4064, "조회 할 수 없는 사용자 입니다."),

    // notification error
    ALARM_NOT_FOUND(HttpStatus.BAD_REQUEST, 4071, "알림을 찾지 못했습니다."),


    //feign error

    FEIGN_CLIENT_ERROR_400(HttpStatus.BAD_REQUEST, 4081, "feign client에서 400 오류가 발생했습니다."),
    FEIGN_CLIENT_ERROR_500(HttpStatus.INTERNAL_SERVER_ERROR, 5001,"feign client에서 500 오류가 발생했습니다."),

    NOT_FOUND_FACILITY(HttpStatus.BAD_REQUEST, 4091, "운동시설 없어");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
        // 결과 예시 - "Validation error - Reason why it isn't valid"
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    public static Code valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) {
            throw new GeneralException("HttpStatus is null.");
        }

        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) {
                        System.out.println("!!!!!!!!!!!!!!!");
                        return Code.BAD_REQUEST;
                    } else if (httpStatus.is5xxServerError()) {
                        return Code.INTERNAL_ERROR;
                    } else {
                        return Code.OK;
                    }
                });
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }
}
