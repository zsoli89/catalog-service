package hu.webuni.catalogservice.service;

import hu.webuni.catalogservice.model.dto.WarehouseDto;
import hu.webuni.catalogservice.model.mapper.WarehouseMapper;
import hu.webuni.catalogservice.repository.WarehouseRepository;
import hu.webuni.commonlib.dto.BaseResponseDto;
import hu.webuni.commonlib.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseService.class);

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper mapper;

    public BaseResponseDto getCurrentProductPerQuantity(OrderDto orderRequestDto) {
        Set<Long> productIdSet = orderRequestDto.getProducts().keySet();
        List<WarehouseDto> byProductIdList = mapper.warehouseToDtoList(warehouseRepository.findByProductIdList(productIdSet));
        LOGGER.info("Products in warehouse list size: {}", byProductIdList.size());
        if (byProductIdList.isEmpty()) {
            LOGGER.error("Couldn't found products in warehouse");
            return new BaseResponseDto(new HashMap<>());
        }
        Map<Long, Long> map = new HashMap<>();
        for (WarehouseDto item : byProductIdList) {
            map.put(item.getProductId(), item.getQuantity());
        }
        return new BaseResponseDto(map);
    }

}
