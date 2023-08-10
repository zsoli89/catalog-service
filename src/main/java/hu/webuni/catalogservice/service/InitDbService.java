package hu.webuni.catalogservice.service;

import hu.webuni.catalogservice.model.entity.Category;
import hu.webuni.catalogservice.model.entity.Product;
import hu.webuni.catalogservice.model.enums.AmountUnits;
import hu.webuni.catalogservice.repository.CategoryRepository;
import hu.webuni.catalogservice.repository.ProductRepository;
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

    @Transactional
    public void deleteDb() {
        productRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
    }

    @Transactional
    public void deleteAudTables() {
        jdbcTemplate.update("DELETE FROM product_aud");
        jdbcTemplate.update("DELETE FROM category_aud");
        jdbcTemplate.update("DELETE FROM category_products_aud");
        jdbcTemplate.update("DELETE FROM revinfo");
    }

    @Transactional
    public void addInitData() {
        Product product1 = createProduct("Pampers", AmountUnits.PCS, "Pampers pelenka Premium Care", 72L, 6990D, 1L, "Újszülött pelenka 2-5kg", null);
        Product product2 = createProduct("Pampers", AmountUnits.PCS, "Pampers pelenka Premium Care", 224L, 19990D, 2L, "4-8kg", null);
        Product product3 = createProduct("Libero", AmountUnits.PCS, "Pampers pelenka Premium Care", 42L, 2990D, 1L, "2-5kg", null);
        Product product4 = createProduct("Kolorky", AmountUnits.PCS, "Kolorky Day Szívecskés ökopelenka", 25L, 3490D, 1L, "3-6kg", null);
        Product product5 = createProduct("Pampers", AmountUnits.PCS, "Pampers pelenka Premium Care", 148L, 19990D, 4L, "11-16kg", null);
        Product product6 = createProduct("Sudocrem", AmountUnits.BOX, "Sudocream Sensitive törlőkendő", 55L, 9990D, null, "törlőkendő", null);
        Product product7 = createProduct("Domestos", AmountUnits.PIECE, "Domestos higiénikus törlőkendő", 1L, 1200D, null, "törlőkendő", "sárga");
        Product product8 = createProduct("Jar", AmountUnits.PIECE, "Jar mosogatószer", 1L, 1890D, null, "mosogatószer", "zöld");

        createCategory("Pelenka", Arrays.asList(product1, product2, product3, product4, product5));
        createCategory("Törlőkendő", Arrays.asList(product6, product7));
        createCategory("Háztartási vegyiáru", Arrays.asList(product7, product8));
    }

    private Category createCategory(String name, List<Product> productList) {
        return categoryRepository.save(
                Category.builder()
                        .name(name)
                        .products(new HashSet<>(productList))
                        .build()
        );
    }

    private Product createProduct(String brand, AmountUnits amountUnits, String name, Long quantity, Double price,
                                  Long size, String description, String color) {
        return productRepository.save(
                Product.builder()
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
