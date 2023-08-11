package hu.webuni.catalogservice.model.mapper;

import hu.webuni.catalogservice.model.dto.ProductDto;
import hu.webuni.catalogservice.model.entity.Product;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product dtoToEntity(ProductDto dto);
    ProductDto entityToDto(Product entity);

    @Named("summary")
    @Mapping(target = "category", ignore = true)
    ProductDto productSummaryToDto(Product product);
    @IterableMapping(qualifiedByName = "summary")
    List<ProductDto> productSummariesToDtoList(Iterable<Product> productList);

    @Named("dtotoentity")
    @Mapping(target = "category", ignore = true)
    Product dtoSummaryToProduct(ProductDto product);
    List<ProductDto> productToDtoList(Iterable<Product> productList);

}
