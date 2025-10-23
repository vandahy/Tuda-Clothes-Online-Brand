package chubby.teu.tuda.feature.order.service.Impl;

import chubby.teu.tuda.core.Payment;
import chubby.teu.tuda.feature.order.dto.OrderDetailRequest;
import chubby.teu.tuda.feature.order.dto.OrderItemRequest;
import chubby.teu.tuda.feature.order.dto.ProductListRequest;
import chubby.teu.tuda.feature.order.repository.OrdersDetailRepository;
import chubby.teu.tuda.feature.order.repository.PaymentRepository;
import chubby.teu.tuda.feature.order.service.OrdersDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersDetailServiceImpl implements OrdersDetailService {
    @Autowired
    OrdersDetailRepository ordersDetailRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    public List<ProductListRequest> getAllPurchasedItems(String username) {
        return ordersDetailRepository.findAllPurchasedItemsByUsername(username);
    }
}
