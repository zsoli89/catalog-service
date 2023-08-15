package hu.webuni.catalogservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class JwtTokenService {

    private final RedisService redisService;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenService.class);
    private final WebshopUserDetailsService userDetailsService;
    private static final String AUTH = "auth";
    private static final String BEARER = "Bearer ";
    private String issuer = "WebshopApp";
    @Value("${redis.user.postfix}")
    private String redisUserPostfix;
    @Value("${redis.user.refresh.postfix}")
    private String redisUserRefreshPostfix;
    @Value("${jwt.secret}")
    private String secret;

    public String generateAccessToken(String username, String roles) {
        logger.info("Generate new access token for user: {}", username);
        String id = saveTokenToRedis(username, redisUserPostfix, 6000L);

        return Jwts
                .builder()
                .setClaims(createClaimsMap(roles))
                .setSubject(username)
                .setIssuer(issuer)
                .setId(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String generateRefreshToken(String username, String roles) {
        logger.info("Generate new refresh token for user: {}", username);
        String id = saveTokenToRedis(username, redisUserRefreshPostfix, 18000l);

        return Jwts
                .builder()
                .setClaims(createClaimsMap(roles))
                .setSubject(username)
                .setIssuer(issuer)
                .setId(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 180000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Map<String, Object> createClaimsMap(String roles) {
        Map<String, Object> claimsMap = new HashMap<>();
        if ((roles != null) && (!roles.isBlank())) {
            claimsMap.put(AUTH, roles);
        }
        return claimsMap;
    }

    private String generateUUID() {
        return UUID
                .randomUUID()
                .toString();
    }

    private String saveTokenToRedis(String username, String keyExt, Long expire) {
        String id = generateUUID();
        String redisKey = username + keyExt;
        redisService.setValueWithExpiration(redisKey, id, expire);
        logger.info("Token stored in Redis, response");
        return id;
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public void validateToken(String token, String keySuffix) {
        logger.info("Getting all claims from token.");
        Claims claims = getAllClaimsFromToken(token);
        final String username = claims.getSubject();
        String redisKey = username + keySuffix;
        String redisResponse = redisService.getValueFromRedis(redisKey);
        List<String> errors = new ArrayList<>();

        if ((redisResponse == null) || (redisResponse.isEmpty())) {
            errors.add("Token doesn't exist in REDIS");
        }
        if (!claims.getIssuer().equalsIgnoreCase(issuer)) {
            errors.add("Issuer is not matched");
        }
        if (!claims.getId().equals(redisResponse)) {
            errors.add("Token is not matched");
        }
        if (!claims.getExpiration().after(new Date())) {
            errors.add("Token is expired");
        }
        if (!errors.isEmpty()) {
            throw new BadCredentialsException(
                    "Error during validating token {%s}".formatted(errors.toString())
            );
        }
    }

    public void checkJwtToken(String jwtToken) {
        if (jwtToken == null || jwtToken.isBlank()) {
            throw new BadCredentialsException("Jwt access token isn't exists");
        }
        if (!jwtToken.startsWith("Bearer ")) {
            logger.warn("JWT Token not begins with \"Bearer \"");
        } else {
            jwtToken = jwtToken.substring(BEARER.length());
        }
        Claims allClaimsFromToken = getAllClaimsFromToken(jwtToken);
        try {
            if ((allClaimsFromToken.getSubject() == null) || (allClaimsFromToken.getSubject().isBlank())) {
                throw new BadCredentialsException("Username in token is null or empty!");
            }
            MDC.put("username", allClaimsFromToken.getSubject());
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Unable to get JWT Token! {%s}".formatted(e.getMessage()), e);
        } catch (ExpiredJwtException ex) {
            throw new AccessDeniedException("JWT access token has expired! {%s}".formatted(ex.getMessage()), ex);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(allClaimsFromToken.getSubject());
        validateToken(jwtToken, redisUserPostfix);
        UsernamePasswordAuthenticationToken authentication = getAuthenticationToken(jwtToken, userDetails);
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
        logger.info("Request token valid, user authentication set!");
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(String token, UserDetails userDetails) {
        Claims claims = getAllClaimsFromToken(token);
        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims
                        .get(AUTH)
                        .toString()
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), userDetails.getPassword(), authorities);
    }

}
