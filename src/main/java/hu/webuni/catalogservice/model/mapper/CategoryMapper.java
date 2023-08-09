package hu.webuni.catalogservice.model.mapper;

import hu.webuni.catalogservice.model.dto.CategoryDto;
import hu.webuni.catalogservice.model.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category dtoToEntity(CategoryDto dto);
    CategoryDto entityToDto(Category entity);
}
