package org.freshkart.oms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.freshkart.ims.entity.Product;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "purchase_order_item")
@Schema(description = "Purchase Order Items")
@Getter
@Setter
public class PurchaseOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_details_id")
    @Schema(required = true)
    private Long orderDetailsId;


    private Integer quantity;

    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "purchase_order_id")
   @JsonBackReference
    private PurchaseOrder purchaseOrder;
    public PurchaseOrderItem() {
    }

}
