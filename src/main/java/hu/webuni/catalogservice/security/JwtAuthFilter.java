package hu.webuni.catalogservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String ACC_TOKEN = "acc_token";
    private final SecurityService securityService;
    private final FreeUriConfiguration freeUriConfiguration;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        freeUriConfiguration.setUriParts();
        if (isProtectedByJwt(freeUriConfiguration.getUriParts(), request.getRequestURI())) {
            String jwtToken = request.getHeader(AUTHORIZATION);
            if (jwtToken == null || jwtToken.isBlank() || !jwtToken.startsWith(BEARER)) {
                logger.error("Not a Bearer token: {%s}".formatted(jwtToken));
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            MDC.put(ACC_TOKEN, jwtToken);
            UsernamePasswordAuthenticationToken authentication = securityService.validateAccessToken(MDC.get(ACC_TOKEN));
            MDC.put("username", authentication.getName());
            if (authentication != null) {
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isProtectedByJwt(List<String> freeUriParts, String uri) {
        boolean isProtectedByJwt = true;
        for (String freeUriPart : freeUriParts) {
            if (uri.contains(freeUriPart)) {
                isProtectedByJwt = false;
                break;
            }
        }
        return isProtectedByJwt;
    }

}
