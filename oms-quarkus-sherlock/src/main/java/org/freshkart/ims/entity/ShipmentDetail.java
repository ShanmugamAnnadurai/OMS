package org.freshkart.ims.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
@Table(name = "shipment_detail")
@Schema(description = "Shipment Detail")
@Getter
@Setter
public class ShipmentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_details_id")
    @Schema(required = true)
    private Long shipmentDetailsId;

    @Column(nullable = false)
    @Schema(required = true)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    @Schema(required = true)
    private Double unitPrice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    public ShipmentDetail() {
    }
}
