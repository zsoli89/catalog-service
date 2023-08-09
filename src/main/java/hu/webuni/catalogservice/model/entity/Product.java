package hu.webuni.catalogservice.model.entity;

import hu.webuni.catalogservice.model.enums.AmountUnits;
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
public class Product {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include()
    private Long id;
    private Double price;
    private String name;
    private Long quantity;
    private AmountUnits amountUnits;
    @ManyToMany(mappedBy = "productList")
    private Set<Category> categoryList;
}
