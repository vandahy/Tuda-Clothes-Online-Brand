package chubby.teu.tuda.feature.orderManager.mapper;

import chubby.teu.tuda.core.Order;
import chubby.teu.tuda.feature.orderManager.dto.OrderDTO;
import chubby.teu.tuda.feature.orderManager.dto.OrderDetailDTO;

import java.util.Collections;
import java.util.stream.Collectors;

public class OrderMapper {
    
    public static OrderDTO toDTO(Order order) {
        if (order == null) return null;
        
        return new OrderDTO(
                order.getOrderCode(),
                order.getUser() != null ? order.getUser().getUsername() : null,
                order.getUser() != null ? order.getUser().getFullName() : null,
                null, // customerEmail - Order entity doesn't have this
                null, // customerPhone - Order entity doesn't have this
                order.getShippingAddress(),
                order.getTotalAmount(),
                order.getStatus() != null ? order.getStatus().name() : null,
                order.getPayment() != null && order.getPayment().getMethod() != null
                    ? order.getPayment().getMethod().name() : null,
                order.getPayment() != null && order.getPayment().getStatus() != null 
                    ? order.getPayment().getStatus().name() : null,
                order.getOrderDate(),
                order.getUpdatedAt(),
                order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()
                    ? order.getOrderDetails().stream()
                        .map(od -> new OrderDetailDTO(
                            od.getProduct() != null ? od.getProduct().getProductCode() : null,
                            od.getProduct() != null ? od.getProduct().getName() : null,
                            od.getVariant() != null ? od.getVariant().getVariantId() : null,
                            od.getVariant() != null && od.getVariant().getSize() != null 
                                ? od.getVariant().getSize().getName() : null,
                            od.getQuantity(),
                            od.getUnitPrice(),
                            od.getDiscount(),
                            od.getSubTotal()
                        ))
                        .collect(Collectors.toList())
                    : Collections.emptyList()
        );
    }
}
