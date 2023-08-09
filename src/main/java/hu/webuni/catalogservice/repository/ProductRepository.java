package hu.webuni.catalogservice.repository;

import hu.webuni.catalogservice.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
