package chubby.teu.tuda.feature.order.repository;

import chubby.teu.tuda.core.Order;
import chubby.teu.tuda.core.OrderDetail;
import chubby.teu.tuda.feature.order.dto.OrderDetailRequest;
import chubby.teu.tuda.feature.order.dto.OrderItemRequest;
import chubby.teu.tuda.feature.order.dto.ProductListRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersDetailRepository extends JpaRepository<OrderDetail, OrderDetail.OrderDetailId> {

        @Query("SELECT new chubby.teu.tuda.feature.order.dto.ProductListRequest(" +
                        "o.product.productCode, " +
                        "o.product.name, " +
                        "(SELECT MIN(img.imageUrl) " +
                        " FROM ProductImage img " +
                        " WHERE img.product = o.product), " +
                        "ord.orderCode, " +
                        "ord.orderDate, " +
                        "o.quantity, " +
                        "o.subTotal) " +
                        "FROM OrderDetail o " +
                        "JOIN o.order ord " +
                        "WHERE ord.user.username = :username " +
                        "ORDER BY ord.orderDate DESC")
        List<ProductListRequest> findAllPurchasedItemsByUsername(@Param("username") String username);

        List<OrderDetail> findByOrder(Order order);
}
