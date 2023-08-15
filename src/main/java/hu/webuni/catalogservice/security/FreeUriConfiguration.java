package hu.webuni.catalogservice.security;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Data
public class FreeUriConfiguration {

    private List<String> uriParts;

    public List<String> getUriParts() {
        return uriParts;
    }

    public FreeUriConfiguration setUriParts() {
        this.uriParts = new ArrayList<>();
        this.uriParts.add("/free/");
        this.uriParts.add("/api/security/free/**");

        return this;
    }
}

