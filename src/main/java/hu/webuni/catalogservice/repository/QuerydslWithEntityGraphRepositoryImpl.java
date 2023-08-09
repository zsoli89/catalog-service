package hu.webuni.catalogservice.repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import hu.webuni.catalogservice.model.entity.Category;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import java.util.List;

public class QuerydslWithEntityGraphRepositoryImpl
        extends SimpleJpaRepository<Category, Long>
        implements QuerydslWithEntityGraphRepository<Category, Long> {

    private final EntityManager entityManager;
    private final EntityPath<Category> path;
    private final PathBuilder<Category> builder;
    private final Querydsl querydsl;

    public QuerydslWithEntityGraphRepositoryImpl(EntityManager em) {
        super(Category.class, em);
        this.entityManager = em;
        this.path = SimpleEntityPathResolver.INSTANCE.createPath(Category.class);
        this.builder = new PathBuilder<>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(em, builder);
    }

    @Override
    public List<Category> findAll(Predicate predicate, String entityGraphName, Sort sort) {
        JPAQuery query =
                (JPAQuery) querydsl.applySorting(sort, createQuery(predicate).select(path));
        query.setHint(EntityGraph.EntityGraphType.LOAD.getKey(), entityManager.getEntityGraph(entityGraphName));
        return query.fetch();
    }

    private JPAQuery createQuery(Predicate predicate) {
        return querydsl.createQuery(path).where(predicate);
    }
}
