package org.freshkart.wms.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;


@Entity
@Table(name = "warehouse_location")
@Schema(description = "Warehouse Location")
@Getter
@Setter
public class WarehouseLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    @Schema(required = true)
    private Long warehouseId;

    @Column(nullable = false)
    @Schema(required = true)
    private String name;

    @Column(nullable = false)
    @Schema(required = true)
    private String location;

    @Column(name = "warehouse_manager")
    private String warehouseManager;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonBackReference
    private List<StorageLocation> storageLocations;

    public WarehouseLocation() {
    }
}
