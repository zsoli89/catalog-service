package hu.webuni.catalogservice.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringExpression;
import hu.webuni.catalogservice.model.entity.Product;
import hu.webuni.catalogservice.model.entity.QCategory;
import hu.webuni.catalogservice.model.entity.QProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends
        JpaRepository<Product, Long>,
        QuerydslPredicateExecutor<Product>,
        QuerydslBinderCustomizer<QProduct>,
        QuerydslWithEntityGraphRepository<Product, Long> {

    @Override
    default void customize(QuerydslBindings bindings, QProduct product) {
        bindings.bind(product.name).first(StringExpression::containsIgnoreCase);
        bindings.bind(product.categories.any().name).first(StringExpression::startsWith);
        bindings.bind(product.price).all((path, values) -> {
            Iterator<? extends Double> iterator = values.iterator();
            if (values.size() == 1) {
                Double exactMatch = iterator.next();
                return Optional.of(path.eq(exactMatch));
            }
            if (values.size() == 2) {
                Double min = iterator.next();
                Double max = iterator.next();
                return Optional.of(path.between(min, max));
            }
            if (values.size() == 3) {
                Double first = iterator.next();
                Double second = iterator.next();
                Double third = iterator.next();
                if (second == -1) {
                    return Optional.of(path.goe(first));
                } else if (second == -2) {
                    return Optional.of(path.loe(third));
                }
            }
            return Optional.empty();
        });
        bindings.bind(product.brand).first(StringExpression::startsWith);
        bindings.bind(product.id).all((path, values) -> {
            if (values.size() == 1) {
                return Optional.of(path.eq(values.iterator().next()));
            }
            if (values.size() == 2) {
                Iterator<? extends Long> iterator = values.iterator();
                Long from = iterator.next();
                Long to = iterator.next();
                return Optional.of(path.between(from, to));
            } else {
                return Optional.empty();
            }
        });
    }

    @EntityGraph(attributePaths = {"categories"})
    @Query("SELECT p FROM Product p")
    List<Product> findAllWithCategories(Predicate predicate, Pageable pageable);
}
