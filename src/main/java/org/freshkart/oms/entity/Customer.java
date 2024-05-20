package org.freshkart.oms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Entity
@Table(name = "customer")
@Schema(description = "Customer")
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(required = true)
    @Column(name = "customer_id")
    private Long customerId;

    @Schema(required = true)
    @Column(length = 30,updatable = false,nullable = false)
    private String name;

    @Column(name = "contact_info")
    private String contactInfo;

    private String address;


    public Customer() {
    }
}
