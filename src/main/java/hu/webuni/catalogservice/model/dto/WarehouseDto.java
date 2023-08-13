package hu.webuni.catalogservice.model.dto;

import hu.webuni.commonlib.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WarehouseDto extends BaseDto {

    private Long id;
    private Long productId;
    private Long quantity;
}
