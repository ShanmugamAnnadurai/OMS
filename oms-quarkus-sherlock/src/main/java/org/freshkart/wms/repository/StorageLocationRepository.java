package org.freshkart.wms.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.freshkart.wms.entity.StorageLocation;

@ApplicationScoped
public class StorageLocationRepository implements PanacheRepository<StorageLocation> {
}
