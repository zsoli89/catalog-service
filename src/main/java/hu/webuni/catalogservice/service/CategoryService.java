package hu.webuni.catalogservice.service;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import hu.webuni.catalogservice.model.dto.CategoryDto;
import hu.webuni.catalogservice.model.entity.Category;
import hu.webuni.catalogservice.model.entity.QCategory;
import hu.webuni.catalogservice.model.mapper.CategoryMapper;
import hu.webuni.catalogservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryDto findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return categoryMapper.entityToDto(category.get());
    }

    public List<CategoryDto> searchCategories(Predicate predicate, Optional<Boolean> full, Pageable pageable) {
        List<Category> categoryList;
        if(full.orElse(false)) {
            Page<Category> categoryPage = categoryRepository.findAll(predicate, pageable);
            BooleanExpression categoryIdInPredicate = QCategory.category.in(categoryPage.getContent());
            categoryList = categoryRepository.findAll(categoryIdInPredicate, "Category.products", pageable.getSort());
            return categoryMapper.categoryToDtoList(categoryList);
        } else {
            return categoryMapper.categorySummariesToDtoList(categoryRepository.findAll(predicate, pageable));
        }

    }
}
