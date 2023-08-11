package hu.webuni.catalogservice.service;

import com.querydsl.core.types.Predicate;
import hu.webuni.catalogservice.model.dto.ProductDto;
import hu.webuni.catalogservice.model.entity.HistoryData;
import hu.webuni.catalogservice.model.entity.Product;
import hu.webuni.catalogservice.model.mapper.CategoryMapper;
import hu.webuni.catalogservice.model.mapper.ProductMapper;
import hu.webuni.catalogservice.repository.CategoryRepository;
import hu.webuni.catalogservice.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public ProductDto create(Product product) {
        Product savedProduct;
        if (product.getCategory() != null) {
            product.setCategory(categoryRepository.findById(product.getCategory().getId()).get());
            savedProduct = productRepository.save(product);
        } else {
            savedProduct = productRepository.save(product);
        }
        logger.info("Product saved by id {}", savedProduct.getId());
        return productMapper.entityToDto(savedProduct);
    }

    @Transactional
    public ProductDto update(ProductDto dto) {
        boolean isExists = productRepository.existsById(dto.getId());
        if (!isExists) {
            logger.error("Couldn't find Product entity to update by id: {}", dto.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Product updatedProduct = productRepository.save(productMapper.dtoToEntity(dto));
        logger.info("Product updated by id {}", updatedProduct.getId());
        return productMapper.entityToDto(updatedProduct);
    }

    @Transactional
    public void delete(Long id) {
        boolean isExists = productRepository.existsById(id);
        if (!isExists) {
            logger.error("Couldn't find Product entity to delete by id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        productRepository.deleteById(id);
        logger.info("Product deleted by id: {}", id);
    }

    public List<ProductDto> searchProducts(Predicate predicate, Pageable pageable) {
        List<ProductDto> productList = productMapper.productSummariesToDtoList(productRepository.findAll(predicate, pageable));
        return productList;
    }

    @Transactional
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Cacheable("productHistory")
    public List<HistoryData<Double>> getHistoryById(Long id) {
        List<HistoryData<Double>> priceHistory = new ArrayList<>();

        AuditReader auditReader = AuditReaderFactory.get(em);
        List<Number> revisions = auditReader.getRevisions(Product.class, id);

        for (int i = 0; i < revisions.size(); i++) {
            Number revision = revisions.get(i);
            Product productRevision = auditReader.find(Product.class, id, revision);
            Double price = productRevision.getPrice();
            Date revisionDate = auditReader.getRevisionDate(revision);

            RevisionType revType = RevisionType.ADD;  // Default, hozzáadás
            if (i > 0) {
                Product prevProductRevision = auditReader.find(Product.class, id, revisions.get(i - 1));
                Double prevPrice = prevProductRevision.getPrice();
                if (!price.equals(prevPrice)) {
                    revType = RevisionType.MOD;  // Módosítás
                }
            }

            priceHistory.add(new HistoryData<>(price, revType, revision.intValue(), revisionDate));
        }
        return priceHistory;
    }

}

