package chubby.teu.tuda.feature.cart.repository;

import chubby.teu.tuda.core.Cart;
import chubby.teu.tuda.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUserAndStatus(@Param("user") User user, @Param("status") Cart.CartStatus status);

    @Query("SELECT c FROM Cart c WHERE c.user.username = :username AND c.status = 'ACTIVE'")
    Optional<Cart> findActiveCartByUsername(@Param("username") String username);
}
