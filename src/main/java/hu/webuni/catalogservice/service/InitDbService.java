package hu.webuni.catalogservice.service;

import hu.webuni.catalogservice.model.entity.Category;
import hu.webuni.catalogservice.model.entity.Product;
import hu.webuni.catalogservice.model.entity.Warehouse;
import hu.webuni.catalogservice.model.enums.AmountUnits;
import hu.webuni.catalogservice.repository.CategoryRepository;
import hu.webuni.catalogservice.repository.ProductRepository;
import hu.webuni.catalogservice.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InitDbService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final JdbcTemplate jdbcTemplate;
    private final WarehouseRepository warehouseRepository;

    @Transactional
    public void deleteDb() {
        productRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
    }

    @Transactional
    public void deleteAudTables() {
        jdbcTemplate.update("DELETE FROM product_aud");
        jdbcTemplate.update("DELETE FROM category_aud");
        jdbcTemplate.update("DELETE FROM revinfo");
    }

    @Transactional
    public void addInitData() {

        Category category1 = createCategory("Pelenka");
        Category category2 = createCategory("Törlőkendő");
        Category category3 = createCategory("Háztartási vegyiáru");

        Product product = createProduct(category1, "Pampers", AmountUnits.PCS, "Pampers pelenka Premium Care", 72L, 6990D, 1L, "Újszülött pelenka 2-5kg", "UNDEFINED");
        Product product1 = createProduct(category1, "Pampers", AmountUnits.PCS, "Pampers pelenka Premium Care", 224L, 19990D, 2L, "4-8kg", "UNDEFINED");
        Product product2 = createProduct(category1, "Libero", AmountUnits.PCS, "Libero Newborn", 42L, 2990D, 1L, "2-5kg", "NON_COLOURED");
        Product product3 = createProduct(category1, "Kolorky", AmountUnits.PCS, "Kolorky Day Szívecskés ökopelenka", 25L, 3490D, 1L, "3-6kg", "UNDEFINED");
        Product product4 = createProduct(category1, "Pampers", AmountUnits.PCS, "Pampers pelenka Premium Care", 148L, 19990D, 4L, "11-16kg", "UNDEFINED");
        Product product5 = createProduct(category2, "Sudocrem", AmountUnits.BOX, "Sudocream Sensitive törlőkendő", 55L, 9990D, 1L, "törlőkendő", "UNDEFINED");
        Product product6 = createProduct(category2, "Domestos", AmountUnits.PIECE, "Domestos higiénikus törlőkendő", 1L, 1200D, 1L, "törlőkendő", "sárga");
        Product product7 = createProduct(category3, "Jar", AmountUnits.PIECE, "Jar mosogatószer", 1L, 1890D, 1L, "mosogatószer", "zöld");

        createWarehouse(product.getId(), 5L);
        createWarehouse(product1.getId(), 5L);
        createWarehouse(product2.getId(), 5L);
        createWarehouse(product3.getId(), 5L);
        createWarehouse(product4.getId(), 5L);
        createWarehouse(product5.getId(), 5L);
        createWarehouse(product6.getId(), 5L);
        createWarehouse(product7.getId(), 5L);
    }

    private Category createCategory(String name) {
        return categoryRepository.save(
                Category.builder()
                        .name(name)
                        .build()
        );
    }

    private Product createProduct(Category category, String brand, AmountUnits amountUnits, String name, Long quantity, Double price,
                                  Long size, String description, String color) {
        return productRepository.save(
                Product.builder()
                        .category(category)
                        .brand(brand)
                        .name(name)
                        .amountUnits(amountUnits)
                        .quantity(quantity)
                        .price(price)
                        .size(size)
                        .description(description)
                        .color(color)
                        .build()
        );
    }

    @Transactional
    public void createWarehouse(Long productId, Long quantity) {
        warehouseRepository.save(
                Warehouse.builder()
                        .productId(productId)
                        .quantity(quantity)
                        .build()
        );
    }

    @Transactional
    public void modifyPriceofProduct() {
        Product product = productRepository.findProductByBrand("Kolorky").get(0);
        product.setPrice(1000D);
        productRepository.save(product);
    }

    @Transactional
    public void modifyPriceofProduct2() {
        Product product = productRepository.findProductByBrand("Kolorky").get(0);
        product.setPrice(1500D);
        productRepository.save(product);
    }

}
