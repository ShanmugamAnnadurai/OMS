package org.freshkart.ims.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Entity
@Table(name = "supplier")
@Schema(description = "Supplier")
@Getter
@Setter
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    @Schema(required = true)
    private Long supplierId;

    @Column(nullable = false)
    @Schema(required = true)
    private String name;

    @Column(name = "contact_info")
    private String contactInfo;

    public Supplier() {
    }

}
