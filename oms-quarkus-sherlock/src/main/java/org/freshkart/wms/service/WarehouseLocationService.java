package org.freshkart.wms.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.freshkart.wms.entity.WarehouseLocation;
import org.freshkart.wms.repository.WarehouseLocationRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WarehouseLocationService {
    @Inject
    WarehouseLocationRepository warehouseLocationRepository;

    public List<WarehouseLocation> getAllWarehouseLocation() {
        return warehouseLocationRepository.listAll();
    }

    public WarehouseLocation getWarehouseLocationById(Long warehouseLocationId) {
        return warehouseLocationRepository.findById(warehouseLocationId);
    }

    @Transactional
    public WarehouseLocation createWarehouseLocation(WarehouseLocation warehouseLocation) {
        warehouseLocationRepository.persist(warehouseLocation);
        return warehouseLocation;
    }

    @Transactional
    public WarehouseLocation updateWarehouseLocation(WarehouseLocation updatedWarehouseLocation) {
        Optional<WarehouseLocation> existingWarehouseLocation =
                Optional.ofNullable(getWarehouseLocationById(updatedWarehouseLocation.getWarehouseId()));
        if (existingWarehouseLocation.isPresent()) {
            return warehouseLocationRepository.getEntityManager().merge(updatedWarehouseLocation);
        } else {
            throw new IllegalArgumentException("WarehouseLocation with ID " + updatedWarehouseLocation.getWarehouseId()+ " does not exist");
        }
    }

    @Transactional
    public boolean deleteWarehouseLocation(Long warehouseLocationId) {
        return warehouseLocationRepository.deleteById(warehouseLocationId);
    }
}
