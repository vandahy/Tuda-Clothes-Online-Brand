package chubby.teu.tuda.feature.cart.repository;

import chubby.teu.tuda.core.Cart;
import chubby.teu.tuda.core.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, CartItem.CartItemId> {

    // Lấy danh sách item theo cart
    List<CartItem> findByCart(Cart cart);

    // Tìm item cụ thể theo cartCode + variantId
    Optional<CartItem> findByCart_CartCodeAndVariant_VariantId(String cartCode, Integer variantId);

    // Xóa 1 item cụ thể theo cartCode + variantId
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.cart.cartCode = :cartCode AND c.variant.variantId = :variantId")
    void deleteByCartCodeAndVariantId(
            @Param("cartCode") String cartCode,
            @Param("variantId") Integer variantId
    );

    // Xóa toàn bộ item trong 1 giỏ hàng
    @Transactional
    void deleteByCart_CartCode(String cartCode);

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cartCode = :cartCode")
    List<CartItem> getItems(@Param("cartCode") String cartCode);
}
