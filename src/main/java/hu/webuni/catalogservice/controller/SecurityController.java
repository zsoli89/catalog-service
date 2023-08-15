package hu.webuni.catalogservice.controller;

import hu.webuni.catalogservice.model.dto.LoginDto;
import hu.webuni.catalogservice.security.SecurityService;
import hu.webuni.catalogservice.security.auth.UsernamePassAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/security")
public class SecurityController {

    private final SecurityService securityService;
    private final UsernamePassAuthService authService;

    @PostMapping("/free/login")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authService.authenticate(loginDto.getUsername(), loginDto.getPassword());
        return securityService.login(loginDto, authentication);
    }

    @GetMapping("/logout/{username}")
    @ResponseStatus(HttpStatus.OK)
    public String logoutByUsername(@PathVariable String username) {
        return securityService.logout(username);
    }
}
