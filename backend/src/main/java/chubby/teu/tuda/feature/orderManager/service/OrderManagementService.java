package chubby.teu.tuda.feature.orderManager.service;

import chubby.teu.tuda.core.Order;
import chubby.teu.tuda.feature.orderManager.dto.OrderDTO;
import chubby.teu.tuda.feature.orderManager.mapper.OrderMapper;
import chubby.teu.tuda.feature.orderManager.repository.OrderManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderManagementService {
    
    @Autowired
    private OrderManagementRepository orderManagementRepository;
    
    public List<OrderDTO> getAllOrders() {
        return orderManagementRepository.findAllWithDetails()
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public OrderDTO getOrderById(String orderCode) {
        Order order = orderManagementRepository.findByIdWithDetails(orderCode)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return OrderMapper.toDTO(order);
    }
    
    public OrderDTO updateOrderStatus(String orderCode, String status) {
        Order order = orderManagementRepository.findById(orderCode)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        try {
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status);
            order.setStatus(newStatus);
            order.setUpdatedAt(LocalDateTime.now());
            
            Order updatedOrder = orderManagementRepository.save(order);
            return OrderMapper.toDTO(updatedOrder);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid order status: " + status);
        }
    }
    
    public void deleteOrder(String orderCode) {
        if (!orderManagementRepository.existsById(orderCode)) {
            throw new RuntimeException("Order not found");
        }
        orderManagementRepository.deleteById(orderCode);
    }
}
