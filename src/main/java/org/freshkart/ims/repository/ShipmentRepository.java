package org.freshkart.ims.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.freshkart.ims.entity.Shipment;

@ApplicationScoped
public class ShipmentRepository implements PanacheRepository<Shipment> {
}
