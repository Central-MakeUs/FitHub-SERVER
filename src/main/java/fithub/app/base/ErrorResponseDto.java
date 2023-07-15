    package fithub.app.base;

    import java.util.List;

    public class ErrorResponseDto extends ResponseDto{

        private ErrorResponseDto(Code errorCode) {
            super(false, errorCode.getCode(), errorCode.getMessage(), null);
        }

        private ErrorResponseDto(Code errorCode, Exception e) {
            super(false, errorCode.getCode(), errorCode.getMessage(e), null);
        }

        private ErrorResponseDto(Code errorCode, String message) {
            super(false, errorCode.getCode(), errorCode.getMessage(message), null);
        }


        public static ErrorResponseDto of(Code errorCode) {
            return new ErrorResponseDto(errorCode);
        }

        public static ErrorResponseDto of(Code errorCode, Exception e) {
            return new ErrorResponseDto(errorCode, e);
        }

        public static ErrorResponseDto of(Code errorCode, String message) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorCode, message);
            return errorResponseDto;
        }
    }
