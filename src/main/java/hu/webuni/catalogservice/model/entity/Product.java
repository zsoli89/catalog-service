package hu.webuni.catalogservice.model.entity;

import hu.webuni.catalogservice.model.enums.AmountUnits;
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
@Audited
public class Product extends BaseEntity {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include()
    private Long id;
    private String brand;
    private String name;
    private Long quantity;
    private Double price;
    private Long size;
    private AmountUnits amountUnits;
    private String description;
    private String color;
    @ManyToMany(mappedBy = "products")
    private Set<Category> categories;
}
