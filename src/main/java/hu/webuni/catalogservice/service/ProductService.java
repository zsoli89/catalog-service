package hu.webuni.catalogservice.service;

import com.querydsl.core.types.Predicate;
import hu.webuni.catalogservice.model.dto.ProductDto;
import hu.webuni.catalogservice.model.entity.Product;
import hu.webuni.catalogservice.model.mapper.ProductMapper;
import hu.webuni.catalogservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> searchProducts(Predicate predicate, Pageable pageable) {
        List<ProductDto> productList = productMapper.productSummariesToDtoList(productRepository.findAll(predicate, pageable));
        return productList;
    }

}

