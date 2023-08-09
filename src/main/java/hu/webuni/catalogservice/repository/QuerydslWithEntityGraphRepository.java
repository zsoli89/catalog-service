package hu.webuni.catalogservice.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface QuerydslWithEntityGraphRepository<T, ID>{

    List<T> findAll(Predicate predicate, String entityGraphName, Sort sort);
}
