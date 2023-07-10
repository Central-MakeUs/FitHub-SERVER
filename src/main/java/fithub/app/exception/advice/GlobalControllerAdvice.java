package fithub.app.exception.advice;

import fithub.app.exception.common.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(IllegalAccessException.class)
    protected ResponseEntity<ResponseFormat> HandlerIllegalAccessException(IllegalAccessException e) {
        System.out.println("IllegalAccessException error");
        ResponseFormat res = new ResponseFormat().of(e);

        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ResponseFormat> HandlerNullPointerException(NullPointerException e) {
        System.out.println("NullPointerException error");
        ResponseFormat res = new ResponseFormat().of(e);

        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ResponseFormat> HandlerException(CustomException e) {
        System.out.println("Exception");
        ResponseFormat res = new ResponseFormat().of(e);;

        return new ResponseEntity<>(res, e.getErrorCode().getStatus());
    }
}
