package org.freshkart.wms.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
@Table(name = "storage_location")
@Schema(description = "Storage Location")
@Getter
@Setter
public class StorageLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    @Schema(required = true)
    private Long locationId;

    @Column(nullable = false)
    @Schema(required = true)
    private String name;

    private Integer capacity;
    @Column(name = "occupied_space")
    private Integer occupiedSpace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name = "warehouse_id")
    private WarehouseLocation warehouse;

    public StorageLocation() {
    }


}
