package repository;

import model.Cart;
import model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CartRepository {
    Optional<Cart> findByUser(User user);
    Cart save(Cart cart);
}
