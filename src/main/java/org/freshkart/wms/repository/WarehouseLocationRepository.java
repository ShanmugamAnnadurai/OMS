package org.freshkart.wms.repository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.freshkart.wms.entity.WarehouseLocation;

@ApplicationScoped
public class WarehouseLocationRepository implements PanacheRepository<WarehouseLocation> {
}
