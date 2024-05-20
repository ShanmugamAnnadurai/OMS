package org.freshkart.wms.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.freshkart.wms.entity.InventoryTransaction;
import org.freshkart.wms.repository.InventoryTransactionRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InventoryTransactionService {


        @Inject
        InventoryTransactionRepository inventoryTransactionRepository;

        public List<InventoryTransaction> getAllInventoryTransaction() {
            return inventoryTransactionRepository.listAll();
        }

        public InventoryTransaction getInventoryTransactionById(Long inventoryTransactionId) {
            return inventoryTransactionRepository.findById(inventoryTransactionId);
        }

        @Transactional
        public InventoryTransaction createInventoryTransaction(InventoryTransaction inventoryTransaction) {
            inventoryTransactionRepository.persist(inventoryTransaction);
            return inventoryTransaction;
        }

        @Transactional
        public InventoryTransaction updateInventoryTransaction(InventoryTransaction updatedInventoryTransaction) {
            Optional<InventoryTransaction> existingInventoryTransaction =
                    Optional.ofNullable(getInventoryTransactionById(updatedInventoryTransaction.getTransactionId()));
            if (existingInventoryTransaction.isPresent()) {
                return inventoryTransactionRepository.getEntityManager().merge(updatedInventoryTransaction);
            } else {
                throw new IllegalArgumentException("InventoryTransaction with ID " + updatedInventoryTransaction.getTransactionId() + " does not exist");
            }
        }

        @Transactional
        public boolean deleteInventoryTransaction(Long inventoryTransactionId) {
            return inventoryTransactionRepository.deleteById(inventoryTransactionId);
        }

}
