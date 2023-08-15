package hu.webuni.catalogservice.securityconfig.repository;


import hu.webuni.catalogservice.securityconfig.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findAppuserByUsername(String username);
    Optional<AppUser> findAppUserByFacebookId(String fbId);
}
