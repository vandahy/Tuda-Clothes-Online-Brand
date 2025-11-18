package chubby.teu.tuda.feature.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class CheckOutRequest {
    private String firstName;
    private String email;
    private String phone;
    private String cartCode;
    private String address;
    private String ward;
    private String city;
    private List<CartItemRequest> items;
}