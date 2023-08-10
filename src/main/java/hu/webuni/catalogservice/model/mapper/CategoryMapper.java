package hu.webuni.catalogservice.model.mapper;

import hu.webuni.catalogservice.model.dto.CategoryDto;
import hu.webuni.catalogservice.model.entity.Category;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category dtoToEntity(CategoryDto dto);
    CategoryDto entityToDto(Category entity);

    @Named("summary")
    @Mapping(target = "products", ignore = true)
    CategoryDto categorySummaryToDto(Category category);

    @IterableMapping(qualifiedByName = "summary")
    List<CategoryDto> categorySummariesToDtoList(Iterable<Category> categoryList);

    List<CategoryDto> categoryToDtoList(Iterable<Category> categoryList);
}
