package chubby.teu.tuda.feature.order.service.Impl;

import chubby.teu.tuda.core.*;
import chubby.teu.tuda.feature.cart.repository.CartItemRepository;
import chubby.teu.tuda.feature.cart.repository.CartRepository;
import chubby.teu.tuda.feature.cart.repository.UserRepository;
import chubby.teu.tuda.feature.order.dto.*;
import chubby.teu.tuda.feature.order.repository.*;
import chubby.teu.tuda.feature.order.service.OrdersService;
import chubby.teu.tuda.feature.productDisplay.repository.ProductRepository;
import chubby.teu.tuda.feature.productDisplay.repository.ProductVariantRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrdersDetailRepository ordersDetailRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ShipmentRepository shipmentRepository;

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest req) {
        // 1. Load cart items
        List<CartItem> items = loadItems(req);
        if (items.isEmpty()) {
            throw new IllegalArgumentException("No items to place order");
        }

        // 2. Lock & check stock
        List<ProductVariant> lockedVariants = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem ci : items) {
            ProductVariant v = productVariantRepository.findById(ci.getVariant().getVariantId())
                    .orElseThrow(() -> new IllegalArgumentException("Variant not found"));
            if (v.getStock() < ci.getQuantity()) {
                throw new RuntimeException("Variant " + v.getVariantId() + " out of stock");
            }
            lockedVariants.add(v);

            // Fetch product for price and details
            Product p = productRepository.findByProductCode(v.getProductCode())
                    .orElseThrow(() -> new IllegalStateException("Product not found for variant"));

            BigDecimal unitPrice = p.getPrice().subtract(p.getDiscount());
            totalAmount = totalAmount.add(unitPrice.multiply(BigDecimal.valueOf(ci.getQuantity())));
        }

        // 3. Create and save Order
        Order order = new Order();
        int randomNumber = new Random().nextInt(1000);
        String formattedCode = String.format("%03d", randomNumber);
        order.setOrderCode("OD" + formattedCode);

        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        order.setUser(user);

        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setTotalAmount(totalAmount);

        PlaceOrderRequest.ShippingAddress shipping = req.getShippingAddress();
        order.setShippingAddress(
                String.format("%s, %s, %s", shipping.getStreet(), shipping.getCity(), shipping.getWard()));

        ordersRepository.save(order);

        // 4. Create and save OrderDetails and update stock
        for (CartItem ci : items) {
            ProductVariant v = lockedVariants.stream()
                    .filter(variant -> variant.getVariantId().equals(ci.getVariant().getVariantId()))
                    .findFirst().get();

            Product p = productRepository.findByProductCode(v.getProductCode()).get();

            OrderDetail od = new OrderDetail();
            od.setOrder(order);
            od.setProduct(p);
            od.setVariant(v); // Lưu variant
            od.setQuantity(ci.getQuantity());
            BigDecimal unitPrice = p.getPrice().subtract(p.getDiscount());
            od.setUnitPrice(unitPrice);
            od.setSubTotal(unitPrice.multiply(BigDecimal.valueOf(ci.getQuantity())));
            od.setDiscount(p.getDiscount() != null ? p.getDiscount() : BigDecimal.ZERO);
            ordersDetailRepository.save(od);

            // Deduct stock
            v.setStock(v.getStock() - ci.getQuantity());
            productVariantRepository.save(v);
        }

        // 5. Deactivate cart
        if (req.getCartCode() != null) {
            cartRepository.findById(req.getCartCode()).ifPresent(cart -> {
                cart.setStatus(Cart.CartStatus.INACTIVE);
                cartRepository.save(cart);
            });
        }

        // 6.Create and save Payments
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(Payment.PaymentMethod.valueOf(req.getPaymentMethod()));
        payment.setAmount(totalAmount);
        payment.setStatus(Payment.PaymentStatus.UNPAID);
        payment.setPaymentDate(LocalDateTime.now());
        String transactionId = null;
        if (payment.getMethod() == Payment.PaymentMethod.MOMO || payment.getMethod() == Payment.PaymentMethod.BANK) {
            transactionId = UUID.randomUUID().toString();
        }
        payment.setTransactionId(transactionId);
        paymentRepository.save(payment);

        // 7.Create and save Shipments
        String shipperName;
        String shipperPhone;
        if (shipping.getCity().equalsIgnoreCase("Hồ Chí Minh")) {
            shipperName = "GiaoHangNhanh";
            shipperPhone = "0912345678";
        } else if (shipping.getCity().equalsIgnoreCase("Hà Nội")) {
            shipperName = "ViettelPost";
            shipperPhone = "0912346789";
        } else {
            shipperName = "VNPost";
            shipperPhone = "0998765435";
        }
        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setShipperName(shipperName);
        shipment.setTrackingNumber(shipperPhone);
        shipment.setStatus(Shipment.ShipmentStatus.PENDING);
        shipment.setDeliveryDate(LocalDate.now());
        shipmentRepository.save(shipment);

        // 8. Create and save Inventory
        for (CartItem ci : items) {
            ProductVariant v = productVariantRepository.findById(ci.getVariant().getVariantId())
                    .orElseThrow(() -> new IllegalArgumentException("Variant not found"));
            Product p = productRepository.findByProductCode(v.getProductCode())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            Inventory inventory = new Inventory();
            inventory.setProduct(p);
            inventory.setType(Inventory.InventoryType.EXPORT);
            inventory.setQuantityChange(-ci.getQuantity());
            inventory.setNote("Order: " + order.getOrderCode());
            inventoryRepository.save(inventory);
        }

        OrderResponse.ShippingAddress responseShippingAddress = new OrderResponse.ShippingAddress();
        responseShippingAddress.setStreet(shipping.getStreet());
        responseShippingAddress.setCity(shipping.getCity());

        OrderResponse resp = new OrderResponse();
        resp.setOrderCode(order.getOrderCode());
        resp.setFullName(user.getFullName());
        resp.setStatus(OrderResponse.OrderStatus.valueOf(order.getStatus().name()));
        resp.setTotalAmount(order.getTotalAmount());
        resp.setCreatedAt(order.getCreatedAt().toInstant(java.time.ZoneOffset.UTC));
        resp.setMessage("Order created successfully");
        resp.setCartCleared(req.getCartCode() != null);
        return resp;
    }

    private List<CartItem> loadItems(PlaceOrderRequest req) {
        if (req == null)
            return List.of();

        if (req.getItems() != null && !req.getItems().isEmpty()) {
            return req.getItems().stream().map(i -> {
                CartItem ci = new CartItem();
                Cart c = new Cart();
                c.setCartCode(req.getCartCode() != null ? req.getCartCode() : "__transient__");
                ci.setCart(c);

                ProductVariant pv = new ProductVariant();
                pv.setVariantId(i.getVariantId());
                ci.setVariant(pv);

                ci.setQuantity(i.getQuantity() != null ? i.getQuantity() : 1);
                return ci;
            }).collect(Collectors.toList());
        }

        if (req.getCartCode() != null) {
            Cart cart = cartRepository.findById(req.getCartCode())
                    .orElseThrow(() -> new IllegalArgumentException("Cart not found: " + req.getCartCode()));
            return cartItemRepository.findByCart(cart);
        }

        return List.of();
    }

    @Override
    public List<OrderListRequest> getOrderList(String username) {
        List<OrderListRequest> orderList = ordersRepository.findOrderListByUserId(username);
        return orderList;
    }

    @Override
    public OrderDetailRequest getOrderDetail(String orderCode) {
        Order order = ordersRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderCode));

        List<OrderDetail> orderDetails = ordersDetailRepository.findByOrder(order);
        Payment payment = paymentRepository.findByOrder(order).orElse(null);

        OrderDetailRequest response = new OrderDetailRequest();
        response.setOrderCode(order.getOrderCode());
        response.setOrderDate(order.getOrderDate());
        response.setOrderStatus(order.getStatus().toString());
        response.setCustomerName(order.getUser().getFullName());
        response.setCustomerPhone(order.getUser().getPhone());
        response.setCustomerAddress(order.getShippingAddress());
        response.setPaymentMethod(payment != null ? payment.getMethod().toString() : "N/A");
        response.setPaymentStatus(payment != null ? payment.getStatus().toString() : "N/A");
        response.setTotalAmount(order.getTotalAmount());
        response.setSubtotal(order.getTotalAmount());
        response.setShippingFee(BigDecimal.ZERO);
        response.setDiscountAmount(BigDecimal.ZERO);

        List<OrderItemRequest> items = orderDetails.stream()
                .map(od -> {
                    // Lấy ProductVariant để có size
                    ProductVariant variant = od.getVariant();
                    String size = variant != null ? variant.getSize().getName() : "N/A";

                    return new OrderItemRequest(
                            od.getProduct().getProductCode(),
                            od.getProduct().getName() + "-" + size,
                            od.getQuantity(),
                            od.getUnitPrice(),
                            od.getDiscount() != null ? od.getDiscount() : BigDecimal.ZERO,
                            od.getSubTotal());
                })
                .collect(Collectors.toList());

        response.setItems(items);
        return response;
    }
}
