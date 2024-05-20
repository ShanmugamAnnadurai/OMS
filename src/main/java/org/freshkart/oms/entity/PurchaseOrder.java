package org.freshkart.oms.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;


import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "purchase_order")
@Schema(description = "Purchase Order")
@Getter
@Setter
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_order_id")
    @Schema(required = true)
    private Long purchaseOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Schema(implementation = String.class,format = "date",required = true)
    @Column(name = "order_date", updatable = false,nullable = false)
    private Instant orderDate;

    @Schema(implementation = String.class,format = "date")
    @Column(name="delivery_date")
    private Instant deliveryDate;

    @Column(name = "total_price")
    private Double totalPrice;

    private String status;

    @Column(name = "payment_method")
    private String paymentMethod;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PurchaseOrderItem> orderItems;

    public PurchaseOrder() {
    }

}
