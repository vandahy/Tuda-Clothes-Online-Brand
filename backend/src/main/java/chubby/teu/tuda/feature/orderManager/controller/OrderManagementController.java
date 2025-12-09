package chubby.teu.tuda.feature.orderManager.controller;

import chubby.teu.tuda.feature.orderManager.dto.OrderDTO;
import chubby.teu.tuda.feature.orderManager.service.OrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "*")
public class OrderManagementController {
    
    @Autowired
    private OrderManagementService orderManagementService;
    
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderManagementService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/{orderCode}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String orderCode) {
        try {
            OrderDTO order = orderManagementService.getOrderById(orderCode);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{orderCode}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable String orderCode,
            @RequestBody Map<String, String> body
    ) {
        try {
            String status = body.get("status");
            OrderDTO updated = orderManagementService.updateOrderStatus(orderCode, status);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{orderCode}")
    public ResponseEntity<?> deleteOrder(@PathVariable String orderCode) {
        try {
            orderManagementService.deleteOrder(orderCode);
            return ResponseEntity.ok("Order deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
