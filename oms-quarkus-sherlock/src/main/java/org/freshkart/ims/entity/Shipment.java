package org.freshkart.ims.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.freshkart.oms.entity.PurchaseOrder;

import java.time.Instant;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "shipment")
@Schema(description = "Shipment")
@Getter
@Setter
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    @Schema(required = true)
    private Long shipmentId;

    @Schema(required = true,implementation = String.class,format = "date")
    @Column(name = "shipment_date", updatable = false,nullable = false)
    private Instant shipmentDate;

    @Schema(implementation = String.class,format = "date")
    @Column(name = "arrival_date")
    private Instant arrivalDate;

    private String status;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @OneToMany
    private List<ShipmentDetail> shipmentDetailList;

    public Shipment() {
    }

}
