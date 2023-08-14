package fithub.app.auth.filter;

import fithub.app.auth.provider.TokenProvider;
import fithub.app.base.Code;
import fithub.app.base.exception.handler.JwtAuthenticationException;
import fithub.app.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = request;
        String jwt = tokenProvider.resolveToken(httpServletRequest);
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt, TokenProvider.TokenType.ACCESS)){

            if(!redisService.validateLogOutedToken(jwt)) {
                System.out.println("stop!!");
                throw new JwtAuthenticationException(Code.JWT_FORBIDDEN);
            }
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else{
            throw new JwtAuthenticationException(Code.JWT_TOKEN_NOT_FOUND);
        }
        filterChain.doFilter(httpServletRequest, response);
    }
}
