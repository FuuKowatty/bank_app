package pl.bartoszmech.BankApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bartoszmech.BankApp.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
