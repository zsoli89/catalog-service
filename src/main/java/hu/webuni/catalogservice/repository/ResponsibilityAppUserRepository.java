package hu.webuni.catalogservice.repository;

import hu.webuni.catalogservice.model.entity.ResponsibilityAppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponsibilityAppUserRepository extends JpaRepository<ResponsibilityAppUser, Long> {

    List<ResponsibilityAppUser> findResponsibilityAppUserByUsername(String username);
}
