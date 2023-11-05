package hu.webuni.catalogservice.service;

import com.querydsl.core.types.Predicate;
import hu.webuni.catalogservice.model.dto.ProductDto;
import hu.webuni.catalogservice.model.entity.Category;
import hu.webuni.catalogservice.model.entity.HistoryData;
import hu.webuni.catalogservice.model.entity.Product;
import hu.webuni.catalogservice.model.mapper.ProductMapper;
import hu.webuni.catalogservice.repository.CategoryRepository;
import hu.webuni.catalogservice.repository.ProductRepository;
import hu.webuni.commonlib.dto.OrderDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
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
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private static final String UNDEFINED = "UNDEFINED";

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public ProductDto create(Product product) {
        Product savedProduct;
        expandProduct(product);
        if (product.getCategory() != null) {
            Optional<Category> optionalCategory = categoryRepository.findById(product.getCategory().getId());
            if (optionalCategory.isEmpty()) {
                logger.error("Couldn't find Category entity with id: {}, to save Product", product.getCategory().getId());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            product.setCategory(optionalCategory.get());
            savedProduct = productRepository.save(product);
            logger.info("Product saved by id {}", savedProduct.getId());
            return productMapper.entityToDto(savedProduct);
        } else {
            savedProduct = productRepository.save(product);
            logger.info("Product saved by id {}", savedProduct.getId());
            return productMapper.productSummaryToDto(product);
        }
    }

    private Product expandProduct(Product product) {
        if (product.getBrand() == null || product.getBrand().isBlank())
            product.setColor(UNDEFINED);
        if (product.getColor() == null || product.getColor().isBlank())
            product.setColor(UNDEFINED);
        if (product.getDescription() == null || product.getDescription().isBlank())
            product.setColor(UNDEFINED);
        return product;
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

    @Transactional
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Cacheable("productHistory")
    public List<HistoryData<Product>> getHistoryById2(Long id) {

        return AuditReaderFactory.get(em)
                .createQuery()
                .forRevisionsOfEntity(Product.class, false, false)
                .add(AuditEntity.property("id").eq(id))
                .getResultList()
                .stream()
                .map(o -> {
                    Object[] objArray = (Object[]) o;
                    DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) objArray[1];
                    Product product = (Product) objArray[0];

                    return new HistoryData<Product>(
                            product,
                            (RevisionType) objArray[2],
                            revisionEntity.getId(),
                            revisionEntity.getRevisionDate()
                    );
                }).toList();
    }

    public List<ProductDto> findByIdList(OrderDto dto) {
        List<Long> idList = new ArrayList<>(dto.getProducts().keySet());
        List<Product> productList = productRepository.findByIdList(idList);
        if(dto.getProducts().size() != (productList.size())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        List<ProductDto> productDtoList = productMapper.productSummariesToDtoList(productList);
        if (productDtoList.size() == dto.getProducts().size()) {
            for (ProductDto productDto : productDtoList) {
                Long id = productDto.getId();
                if (dto.getProducts().containsKey(id)) {
                    Long quantity = dto.getProducts().get(id);
                    productDto.setOrderedQuantity(quantity);
                }
            }
        }
        logger.info("Product entities found by ids, list size: {}", productDtoList.size());
        return productDtoList;

    }
}

