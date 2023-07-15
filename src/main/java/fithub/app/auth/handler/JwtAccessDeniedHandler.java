package fithub.app.auth.handler;

import fithub.app.auth.filter.JwtRequestFilter;
import fithub.app.base.Code;
import fithub.app.base.exception.common.ApiErrorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(JwtAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(403);
        PrintWriter writer = response.getWriter();

        ApiErrorResult apiErrorResult = ApiErrorResult.builder()
                .isSuccess(false)
                .code(Code.FORBIDDEN.getCode())
                .message(Code.FORBIDDEN.getMessage())
                .result(null)
                .build();
        try{
            writer.write(apiErrorResult.toString());
        }catch(NullPointerException e){
            LOGGER.error("응답 메시지 작성 에러", e);
        }finally{
            if(writer != null) {
                writer.flush();
                writer.close();
            }
        }

    }
}
