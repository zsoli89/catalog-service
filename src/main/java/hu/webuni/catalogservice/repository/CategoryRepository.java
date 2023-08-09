package hu.webuni.catalogservice.repository;

import hu.webuni.catalogservice.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
