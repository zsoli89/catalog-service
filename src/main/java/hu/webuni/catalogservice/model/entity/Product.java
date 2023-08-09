package hu.webuni.catalogservice.model.entity;

import hu.webuni.catalogservice.model.enums.AmountUnits;
import hu.webuni.commonlib.base.BaseEntity;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include()
    private Long id;
    private Double price;
    private String name;
    private Long quantity;
    private AmountUnits amountUnits;
    private String description;
    private String color;
    @ManyToMany(mappedBy = "products")
    private Set<Category> categories;
}
