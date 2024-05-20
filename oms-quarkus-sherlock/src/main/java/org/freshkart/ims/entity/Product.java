package org.freshkart.ims.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
@Table(name="product")
@Schema(description = "Product")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(required = true)
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false)
    @Schema(required = true)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(name = "quantity_available")
    private  Integer quantityAvailable;

    @Column(name = "reorder_point")
    private Integer reorderPoint;


    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    public Product() {
    }
}
