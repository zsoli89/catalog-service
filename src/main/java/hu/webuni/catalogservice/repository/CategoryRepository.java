package hu.webuni.catalogservice.repository;

import com.querydsl.core.types.dsl.StringExpression;
import hu.webuni.catalogservice.model.entity.Category;
import hu.webuni.catalogservice.model.entity.QCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Iterator;
import java.util.Optional;

public interface CategoryRepository extends
        JpaRepository<Category, Long>,
        QuerydslPredicateExecutor<Category>,
        QuerydslBinderCustomizer<QCategory>,
        QuerydslWithEntityGraphRepository<Category, Long> {

    @Override
    default void customize(QuerydslBindings bindings, QCategory category) {
        bindings.bind(category.name).first(StringExpression::startsWithIgnoreCase);
        bindings.bind(category.products.any().name).first(StringExpression::contains);
        bindings.bind(category.products.any().brand).first(StringExpression::startsWith);
        bindings.bind(category.id).all((path, values) -> {
            if (values.size() == 1)
                return Optional.of(path.eq(values.iterator().next()));
            if (values.size() == 2) {
                Iterator<? extends Long> iterator = values.iterator();
                Long from = iterator.next();
                Long to = iterator.next();
                return Optional.of(path.between(from, to));
            } else {
                return Optional.empty();
            }
        });
        bindings.bind(category.products.any().id).all((path, values) -> {
            if (values.size() == 1)
                return Optional.of(path.eq(values.iterator().next()));
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
}
