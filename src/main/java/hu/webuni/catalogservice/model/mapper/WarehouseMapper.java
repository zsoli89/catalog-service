package hu.webuni.catalogservice.model.mapper;

import hu.webuni.catalogservice.model.dto.WarehouseDto;
import hu.webuni.catalogservice.model.entity.Warehouse;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

    Warehouse dtoToEntity(WarehouseDto dto);
    WarehouseDto entityToDto(Warehouse entity);
    List<WarehouseDto> warehouseToDtoList(List<Warehouse> entityList);

}
