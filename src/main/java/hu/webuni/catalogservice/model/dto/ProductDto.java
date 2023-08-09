package hu.webuni.catalogservice.model.dto;

import hu.webuni.catalogservice.model.enums.AmountUnits;
import hu.webuni.commonlib.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProductDto extends BaseDto {

    private Long id;
    private Double price;
    private String name;
    private Long quantity;
    private AmountUnits amountUnits;
    private String description;
    private String color;
}
