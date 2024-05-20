package org.freshkart.wms.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.freshkart.wms.entity.StorageLocation;
import org.freshkart.wms.repository.StorageLocationRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class StorageLocationService {
    @Inject
    StorageLocationRepository storageLocationRepository;

    public List<StorageLocation> getAllStorageLocation() {
        return storageLocationRepository.listAll();
    }

    public StorageLocation getStorageLocationById(Long storageLocationId) {
        return storageLocationRepository.findById(storageLocationId);
    }

    @Transactional
    public StorageLocation createStorageLocation(StorageLocation storageLocation) {
        storageLocationRepository.persist(storageLocation);
        return storageLocation;
    }

    @Transactional
    public StorageLocation updateStorageLocation(StorageLocation updatedStorageLocation) {
        Optional<StorageLocation> existingStorageLocation =
                Optional.ofNullable(getStorageLocationById(updatedStorageLocation.getLocationId()));
        if (existingStorageLocation.isPresent()) {
            return storageLocationRepository.getEntityManager().merge(updatedStorageLocation);
        } else {
            throw new IllegalArgumentException("Storage Location with ID " + updatedStorageLocation.getLocationId() + " does not exist");
        }
    }

    @Transactional
    public boolean deleteStorageLocation(Long storageLocationId) {
        return storageLocationRepository.deleteById(storageLocationId);
    }

}
