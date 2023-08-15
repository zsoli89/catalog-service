package hu.webuni.catalogservice.security;

import hu.webuni.catalogservice.model.entity.AppUser;
import hu.webuni.catalogservice.model.entity.ResponsibilityAppUser;
import hu.webuni.catalogservice.repository.AppUserRepository;
import hu.webuni.catalogservice.repository.ResponsibilityAppUserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.UserPrincipal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WebshopUserDetailsService implements UserDetailsService {


    private final AppUserRepository userRepository;

    private final ResponsibilityAppUserRepository responsibilityRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser webshopUser = userRepository.findAppuserByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));
        return createUserDetails(webshopUser);
    }

    public List<String> getRoles(String username) {
        List<ResponsibilityAppUser> responsibilityAppUserList = responsibilityRepository.findResponsibilityAppUserByUsername(username);
        List<String> roles = new ArrayList<>();
        for(ResponsibilityAppUser item : responsibilityAppUserList)
            roles.add(item.getRole());
        return roles;
    }

    public UserDetails createUserDetails(AppUser appUser) {
        List<SimpleGrantedAuthority> roles = getRoles(appUser.getUsername())
                .stream()
                .map(SimpleGrantedAuthority::new).toList();

        return new User(appUser.getUsername(), appUser.getPassword(), roles);
    }

}
