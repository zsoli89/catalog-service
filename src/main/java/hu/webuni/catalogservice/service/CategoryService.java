package hu.webuni.catalogservice.service;

import hu.webuni.catalogservice.model.dto.CategoryDto;
import hu.webuni.catalogservice.model.entity.Category;
import hu.webuni.catalogservice.model.mapper.CategoryMapper;
import hu.webuni.catalogservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryDto findById(Long id, Optional<Boolean> full) {
        Optional<Category> category;

        if (full.orElse(false)) {
            category = categoryRepository.findByIdWithRelations(id);
        } else {
            category = categoryRepository.findById(id);
        }
        if (category.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (full.orElse(false))
            return categoryMapper.entityToDto(category.get());
        else
            return categoryMapper.categorySummaryToDto(category.get());
    }

    public CategoryDto createCategory(CategoryDto dto) {
        List<Category> categoryList = categoryRepository.findAll();
        List<Category> matchedCategories = categoryList.stream().filter(c -> c.getName().equals(dto.getName())).toList();
        if (!matchedCategories.isEmpty()) {
            logger.error("Category already exists with name: {}", dto.getName());
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        Category savedCategory = categoryRepository.save(categoryMapper.dtoToEntity(dto));
        logger.info("Category saved by id {}", savedCategory.getId());
        return categoryMapper.entityToDto(savedCategory);
    }

    public List<CategoryDto> findAllWithRelationships(Optional<Boolean> full, Pageable pageable) {
        List<Category> categoryList;
        if (full.orElse(false)) {
            categoryList = categoryRepository.findAll();
            List<Long> idList = categoryList.stream().map(Category::getId).toList();
            categoryList = categoryRepository.findAllWithRelationsPageable(idList, pageable);
            return categoryMapper.categoryToDtoList(categoryList);
        } else {
            return categoryMapper.categorySummariesToDtoList(categoryRepository.findAll(pageable));
        }
    }

}
