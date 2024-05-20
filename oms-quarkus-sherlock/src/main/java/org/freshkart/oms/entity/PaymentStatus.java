package org.freshkart.oms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
@Table(name = "payment_status")
@Schema(description = "Payment Status")
@Getter
@Setter
public class PaymentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    @Schema(required = true)
    private Long paymentId;

    @Schema(required = true)
    @Column(name = "send_city", updatable = false,nullable = false)
    private String sendCity;

    @Column(name = "receiver_city")
    private String receiverCity;

    @Column(name = "volume_fee")
    private Double volumeFee;

    @Column(name = "tax_id", updatable = false)
    private String taxId;

    @OneToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    public PaymentStatus() {
    }

}
