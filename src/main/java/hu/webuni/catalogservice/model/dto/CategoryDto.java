package hu.webuni.catalogservice.model.dto;

import hu.webuni.commonlib.base.BaseDto;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CategoryDto extends BaseDto {

    private Long id;
    private String name;
    private Set<ProductDto> products;

}
