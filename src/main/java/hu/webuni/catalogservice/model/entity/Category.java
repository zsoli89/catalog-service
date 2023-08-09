package hu.webuni.catalogservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Category {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include()
    private Long id;
    private String name;
    @ManyToMany
    private Set<Product> productList;
}
