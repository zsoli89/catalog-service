package hu.webuni.catalogservice.service;

import hu.webuni.catalogservice.model.entity.Category;
import hu.webuni.catalogservice.model.entity.Product;
import hu.webuni.catalogservice.model.enums.AmountUnits;
import hu.webuni.catalogservice.repository.CategoryRepository;
import hu.webuni.catalogservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InitDbService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void deleteDb() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Transactional
    public void addInitData() {
    }

    private Category createCategory(String name, List<Product> productList) {
        return categoryRepository.save(
                Category.builder()
                        .name(name)
                        .products(new HashSet<>(productList))
                        .build()
        );
    }

    private Product createProduct(AmountUnits amountUnits, String name, Long quantity, Double price) {
        return productRepository.save(
                Product.builder()
                        .amountUnits(amountUnits)
                        .name(name)
                        .quantity(quantity)
                        .price(price)
                        .build()
        );
    }
}
