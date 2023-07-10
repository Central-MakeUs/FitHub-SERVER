package fithub.app.exception.advice;

import fithub.app.exception.common.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseFormat {
    private Integer code;
    private String message;
    private Object result;

    @Getter
    private class ErrorResponse {
        private String errorName;

        public ErrorResponse(Exception error) {
            errorName = error.toString();
        }
    }

    public ResponseFormat of(Object data) {
        this.result = data;
        return this;
    }

    public ResponseFormat of(Exception error) {
        this.code = 500;
        this.message = "오류가 발생했습니다.";
        result = new ErrorResponse(error);
        return this;
    }

    public ResponseFormat of(CustomException error) {
        this.code = error.getErrorCode().getCode();
        this.message = error.getMessage();
        result = new ErrorResponse(error);
        return this;
    }
}
