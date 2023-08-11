package hu.webuni.catalogservice.model.mapper;

import hu.webuni.catalogservice.model.dto.CategoryDto;
import hu.webuni.catalogservice.model.dto.ProductDto;
import hu.webuni.catalogservice.model.entity.Category;
import hu.webuni.catalogservice.model.entity.Product;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category dtoToEntity(CategoryDto dto);
    CategoryDto entityToDto(Category entity);

    @Named("summary")
    @Mapping(target = "products", ignore = true)
    CategoryDto categorySummaryToDto(Category category);

    @IterableMapping(qualifiedByName = "summary")
    List<CategoryDto> categorySummariesToDtoList(Iterable<Category> categoryList);
    List<Category> dtoListToCategoryList(List<CategoryDto> dtoList);

    List<CategoryDto> categoryToDtoList(Iterable<Category> categoryList);

//    @IterableMapping(qualifiedByName = "summary")
//    Set<CategoryDto> categorySummariesToDtoSet(Set<Category> categoryList);
    Set<CategoryDto> categoryToDtoSet(Set<Category> categoryList);

    @Mapping(target = "category", ignore = true)
    ProductDto productToDto(Product product);
}
