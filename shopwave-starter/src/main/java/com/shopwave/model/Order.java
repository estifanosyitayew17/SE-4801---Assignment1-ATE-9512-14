// Estifanos Yitayew - ATE/9512/14
package com.shopwave.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String orderNumber;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    private BigDecimal totalAmount;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    
    // Convenience method
    public void addItem(Product product, int quantity) {
        OrderItem item = OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .unitPrice(product.getPrice())
                .order(this)
                .build();
        items.add(item);
        
        // Recalculate total amount
        this.totalAmount = items.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}