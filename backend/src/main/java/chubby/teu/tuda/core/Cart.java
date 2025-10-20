package chubby.teu.tuda.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @Column(name = "cartCode", length = 10)
    private String cartCode;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CartStatus status = CartStatus.INACTIVE;

    @Column(name = "createdAt")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

    public enum CartStatus {
        ACTIVE, INACTIVE
    }
}