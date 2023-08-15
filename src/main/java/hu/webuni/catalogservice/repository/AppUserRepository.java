package hu.webuni.catalogservice.repository;

import hu.webuni.catalogservice.model.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findAppuserByUsername(String username);
    Optional<AppUser> findAppUserByFacebookId(String fbId);
}
