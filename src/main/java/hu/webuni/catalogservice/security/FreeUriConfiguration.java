package hu.webuni.catalogservice.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration
public class FreeUriConfiguration {

    private List<String> uriParts;

    public FreeUriConfiguration setUriParts() {
        this.uriParts = new ArrayList<>();
        this.uriParts.add("/free/");
        this.uriParts.add("/api/security/free/**");

        return this;
    }
}

