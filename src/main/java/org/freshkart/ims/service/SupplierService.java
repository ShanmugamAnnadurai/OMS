package org.freshkart.ims.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.freshkart.ims.entity.Supplier;
import org.freshkart.ims.repository.SupplierRepository;


import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SupplierService {
    @Inject
    SupplierRepository supplierRepository;

    public List<Supplier> getAllSupplier() {
        return supplierRepository.listAll();
    }

    public Supplier getSupplierById(Long supplierId) {
        return supplierRepository.findById(supplierId);
    }

    @Transactional
    public Supplier createSupplier(Supplier supplier) {
        supplierRepository.persist(supplier);
        return supplier;
    }

    @Transactional
    public Supplier updateSupplier(Supplier updatedSupplier) {
        Optional<Supplier> existingSupplier = Optional.ofNullable(getSupplierById(updatedSupplier.getSupplierId()));
        if (existingSupplier.isPresent()) {
            return supplierRepository.getEntityManager().merge(updatedSupplier);
        } else {
            throw new IllegalArgumentException("Supplier with ID " + updatedSupplier.getSupplierId() + " does not exist");
        }
    }

    @Transactional
    public boolean deleteSupplier(Long supplierId) {
        return supplierRepository.deleteById(supplierId);
    }
}
