package chubby.teu.tuda.feature.order.service;

import chubby.teu.tuda.feature.order.dto.OrderListRequest;
import chubby.teu.tuda.feature.order.dto.OrderResponse;
import chubby.teu.tuda.feature.order.dto.PlaceOrderRequest;
import chubby.teu.tuda.feature.order.dto.OrderDetailRequest;

import java.util.List;

public interface OrdersService {
    OrderResponse placeOrder(PlaceOrderRequest req);

    List<OrderListRequest> getOrderList(String name);

    OrderDetailRequest getOrderDetail(String orderCode);
}
