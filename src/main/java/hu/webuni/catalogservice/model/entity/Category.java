package hu.webuni.catalogservice.model.entity;

import hu.webuni.commonlib.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@NamedEntityGraph(
        name = "Category.products",
        attributeNodes = @NamedAttributeNode("products")
)
@Audited
public class Category extends BaseEntity {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include()
    private Long id;
    private String name;
    @ManyToMany
    private Set<Product> products;
}
