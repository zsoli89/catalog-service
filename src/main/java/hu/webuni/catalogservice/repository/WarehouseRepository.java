package hu.webuni.catalogservice.repository;

import hu.webuni.catalogservice.model.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query("SELECT w FROM Warehouse w WHERE w.productId IN :productIdList")
    List<Warehouse> findByProductIdList(@Param("productIdList") Set<Long> productIdList);
}
