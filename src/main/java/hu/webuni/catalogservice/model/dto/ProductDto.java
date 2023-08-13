package hu.webuni.catalogservice.model.dto;

import hu.webuni.catalogservice.model.enums.AmountUnits;
import hu.webuni.commonlib.base.BaseDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProductDto extends BaseDto {

    private Long id;
    private String brand;
    @NotEmpty
    private String name;
    private Double price;
    private Long quantity;
    private Long size;
    private AmountUnits amountUnits;
    private String description;
    private String color;
    private CategoryDto category;
    private Long orderedQuantity;

}
