package chubby.teu.tuda.feature.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderRequest {
    @NotBlank
    private String idempotencyKey;

    private String username;

    private String cartCode;
    private List<Item> items;

    @NotNull
    private ShippingAddress shippingAddress;

    private String billingAddressId;

    @NotBlank
    private String paymentMethod;
    private String paymentToken;

    private String couponCode;
    private String shippingMethodId;
    private String note;
    private Boolean saveAddress = false;

    @Data
    public static class Item {
        @NotNull
        private Integer variantId;
        private String productCode;
        @Min(1)
        private Integer quantity;
    }

    @Data
    public static class ShippingAddress {
        @NotBlank
        private String fullName;
        @NotBlank
        private String phone;
        @NotBlank
        private String street;
        @NotBlank
        private String city;
        @NotBlank
        private String ward;
        @NotBlank
        private String district;

    }
}
