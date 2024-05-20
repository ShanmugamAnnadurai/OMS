package org.freshkart.wms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.freshkart.ims.entity.Product;

import java.time.Instant;


@Entity
@Table(name = "inventory_transactions")
@Schema(description = "Inventory Transaction")
@Getter
@Setter
public class InventoryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    @Schema(required = true)
    private Long transactionId;

    @Column(name = "transaction_date", nullable = false)
    @Schema(required = true,implementation = String.class,format = "date")
    private Instant transactionDate;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(nullable = false)
    @Schema(required = true)
    private Integer quantity;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private StorageLocation location;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public InventoryTransaction() {
    }
}
