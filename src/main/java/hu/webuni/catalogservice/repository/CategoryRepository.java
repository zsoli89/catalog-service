package hu.webuni.catalogservice.repository;

import hu.webuni.catalogservice.model.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @EntityGraph(attributePaths = {"products"})
    @Query("SELECT c FROM Category c WHERE c.id IN :ids")
    List<Category> findAllWithRelationsPageable(List<Long> ids, Pageable pageable);

    @EntityGraph(attributePaths = {"products"})
    @Query("SELECT c FROM Category c WHERE c.id=?1")
    Optional<Category> findByIdWithRelations(Long id);
}
