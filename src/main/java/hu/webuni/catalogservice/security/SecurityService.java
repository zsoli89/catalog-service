package hu.webuni.catalogservice.security;

import hu.webuni.catalogservice.model.dto.LoginDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final WebshopUserDetailsService webshopUserDetailsService;
    private final JwtTokenService jwtTokenService;
    private final RedisService redisService;
    @Value("${redis.user.postfix}")
    private String redisUserPostfix;
    @Value("${redis.user.refresh.postfix}")
    private String redisUserRefreshPostfix;

    public Map<String, String> login(LoginDto loginDto, Authentication authentication) {
        try {
            Map<String, String> tokenMap = new HashMap<>();
            String username = loginDto.getUsername();
            logger.info("Authentication set for user: {}", authentication.getName());
            MDC.put("username", authentication.getName());
            String id = redisService.getValueFromRedis(username + redisUserPostfix);
            if (id == null || id.isEmpty()) {
                String accessToken = jwtTokenService.generateAccessToken(authentication.getName(), authentication.getAuthorities().toString());
                String refreshToken = jwtTokenService.generateRefreshToken(authentication.getName(), authentication.getAuthorities().toString());
                tokenMap.put("accessToken", accessToken);
                tokenMap.put("refreshToken", refreshToken);
                return tokenMap;
            } else {
                logger.error("Duplicate login! Username: {}", username);
                throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT);
            }
        } catch (BadCredentialsException | ResponseStatusException e) {
            logger.error("Error during login. message: {}", e.getMessage());
            throw e;
        }
    }

    public String logout(String username) {
        try {
            if (!MDC.get("username").equals(username))
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            redisService.deleteFromRedis(username + redisUserPostfix);
            redisService.deleteFromRedis(username + redisUserRefreshPostfix);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        logger.info("{} user successfully logged out.", username);
        return "LOGGED OUT";
    }

    public UsernamePasswordAuthenticationToken validateAccessToken(String token) {
        token = tokenBearerRemover(token);
        if (token == null)
            return null;
        Claims allClaims = jwtTokenService.getAllClaimsFromToken(token);
        UserDetails userDetails = webshopUserDetailsService.loadUserByUsername(allClaims.getSubject());
        jwtTokenService.validateToken(token, redisUserPostfix);
        return jwtTokenService.getAuthenticationToken(token, userDetails);
    }

    private String tokenBearerRemover(String token) {
        logger.info("Token bearer remover on: {}",token);
        if (token.startsWith(BEARER)) {
            return token.substring(BEARER.length());
        } else {
            return null;
        }
    }

}
