package hu.webuni.catalogservice.controller;

import hu.webuni.catalogservice.service.WarehouseService;
import hu.webuni.commonlib.dto.BaseResponseDto;
import hu.webuni.commonlib.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping("/get-current-product-quantity")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponseDto getCurrentProductPerQuantity(@RequestBody OrderDto orderRequestDto) {
        return warehouseService.getCurrentProductPerQuantity(orderRequestDto);
    }
}
