package hu.webuni.catalogservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AppUser {

    @Id
    @GeneratedValue
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long id;

    private String name;
    private LocalDate birthdate;

    private String username;
    private String password;
    private String facebookId;

}
