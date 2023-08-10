package hu.webuni.catalogservice.model.dto;

import hu.webuni.catalogservice.model.entity.Category;
import hu.webuni.catalogservice.model.enums.AmountUnits;
import hu.webuni.commonlib.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProductDto extends BaseDto {

    private Long id;
    private String brand;
    private String name;
    private Double price;
    private Long quantity;
    private Long size;
    private AmountUnits amountUnits;
    private String description;
    private String color;
    private Set<Category> categories;
}
