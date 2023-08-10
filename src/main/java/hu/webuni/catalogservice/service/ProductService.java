package hu.webuni.catalogservice.service;

import com.querydsl.core.types.Predicate;
import hu.webuni.catalogservice.model.dto.ProductDto;
import hu.webuni.catalogservice.model.entity.HistoryData;
import hu.webuni.catalogservice.model.entity.Product;
import hu.webuni.catalogservice.model.mapper.ProductMapper;
import hu.webuni.catalogservice.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    @PersistenceContext
    private EntityManager em;

    public List<ProductDto> searchProducts(Predicate predicate, Pageable pageable) {
        List<ProductDto> productList = productMapper.productSummariesToDtoList(productRepository.findAll(predicate, pageable));
        return productList;
    }

    @Transactional
    @SuppressWarnings({"rawtypes", "unchecked"})
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

