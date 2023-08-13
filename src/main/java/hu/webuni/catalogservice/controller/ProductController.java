package hu.webuni.catalogservice.controller;

import com.querydsl.core.types.Predicate;
import hu.webuni.catalogservice.model.dto.ProductDto;
import hu.webuni.catalogservice.model.entity.HistoryData;
import hu.webuni.catalogservice.model.entity.Product;
import hu.webuni.catalogservice.model.mapper.ProductMapper;
import hu.webuni.catalogservice.service.ProductService;
import hu.webuni.commonlib.dto.OrderDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto create(@Valid @RequestBody ProductDto dto) {
        return productService.create(productMapper.dtoToEntity(dto));
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto update(@RequestBody ProductDto dto) {
        return productService.update(dto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> searchProducts(
            @QuerydslPredicate(root = Product.class) Predicate predicate,
            @SortDefault("id") Pageable pageable) {
        return productService.searchProducts(predicate, pageable);
    }

    @GetMapping("/history/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<HistoryData<Double>> getHistory(@PathVariable Long id) {
        return productService.getHistoryById(id);
    }

    @PostMapping("/findAll/product-id-list")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> findByIdList(@RequestBody OrderDto dto) {
        return productService.findByIdList(dto);
    }

}
