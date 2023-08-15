package hu.webuni.catalogservice.securityconfig;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SecurityService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
    private static final String BEARER = "Bearer ";
    private final JwtTokenService jwtTokenService;
    private final RedisService redisService;
    @Value("${redis.user.postfix}")
    private String redisUserPostfix;
    @Value("${redis.user.refresh.postfix}")
    private String redisUserRefreshPostfix;


    public UsernamePasswordAuthenticationToken validateAccessToken(String token) {
        token = tokenBearerRemover(token);
        if (token == null)
            return null;
        UserDetails principal = jwtTokenService.parseJwt(token);
        jwtTokenService.validateToken(token, principal, redisUserPostfix);

        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }

    private String tokenBearerRemover(String token) {
        logger.info("Token bearer remover on: {}",token);
        if (token != null && token.startsWith(BEARER)) {
            return token.substring(BEARER.length());
        } else {
            return null;
        }
    }

}
