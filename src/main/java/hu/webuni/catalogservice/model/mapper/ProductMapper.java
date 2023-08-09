package hu.webuni.catalogservice.model.mapper;

import hu.webuni.catalogservice.model.dto.ProductDto;
import hu.webuni.catalogservice.model.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product dtoToEntity(ProductDto dto);
    ProductDto entityToDto(Product entity);
}
