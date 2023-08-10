package hu.webuni.catalogservice.controller;

import com.querydsl.core.types.Predicate;
import hu.webuni.catalogservice.model.dto.CategoryDto;
import hu.webuni.catalogservice.model.entity.Category;
import hu.webuni.catalogservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/find/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> searchCategories(
            @QuerydslPredicate(root = Category.class) Predicate predicate,
            @RequestParam Optional<Boolean> full,
            @SortDefault("id") Pageable pageable) {
        return categoryService.searchCategories(predicate, full, pageable);
    }
}
