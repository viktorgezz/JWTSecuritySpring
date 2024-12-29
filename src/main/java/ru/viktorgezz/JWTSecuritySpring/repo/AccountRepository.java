package ru.viktorgezz.JWTSecuritySpring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.viktorgezz.JWTSecuritySpring.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
