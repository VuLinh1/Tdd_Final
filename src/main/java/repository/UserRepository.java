package repository;

import model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Integer id);
}
