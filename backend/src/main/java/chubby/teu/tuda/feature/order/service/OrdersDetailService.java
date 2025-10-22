package chubby.teu.tuda.feature.order.service;

import chubby.teu.tuda.feature.order.dto.OrderDetailRequest;
import chubby.teu.tuda.feature.order.dto.ProductListRequest;

import java.util.List;

public interface OrdersDetailService {
//    OrderDetailRequest getOrderDetail(String orderCode);
    List<ProductListRequest> getAllPurchasedItems(String username);
}
