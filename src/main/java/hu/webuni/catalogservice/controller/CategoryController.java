package hu.webuni.catalogservice.controller;

import hu.webuni.catalogservice.model.dto.CategoryDto;
import hu.webuni.catalogservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public CategoryDto findById(
            @PathVariable Long id,
            @RequestParam Optional<Boolean> full
    ) {
        return categoryService.findById(id, full);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto create(@RequestBody CategoryDto dto) {
        return categoryService.createCategory(dto);
    }

    @GetMapping("/findall")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> searchCategories(
            @RequestParam Optional<Boolean> full,
            @SortDefault("id") Pageable pageable) {
        return categoryService.findAllWithRelationships(full, pageable);
    }
}
