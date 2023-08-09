package hu.webuni.catalogservice.model.dto;

import hu.webuni.commonlib.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class CategoryDto extends BaseDto {

    private Long id;

    private String name;
    private Set<ProductDto> products;

}
