package org.freshkart.wms.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.freshkart.ims.entity.Product;
import org.freshkart.ims.service.ProductService;
import org.freshkart.wms.entity.InventoryTransaction;
import org.freshkart.wms.entity.StorageLocation;
import org.freshkart.wms.repository.InventoryTransactionRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InventoryTransactionService {


        @Inject
        InventoryTransactionRepository inventoryTransactionRepository;
        @Inject
        ProductService productService;

        @Inject
        StorageLocationService storageLocationService;

        public List<InventoryTransaction> getAllInventoryTransaction() {
            return inventoryTransactionRepository.listAll();
        }

        public InventoryTransaction getInventoryTransactionById(Long inventoryTransactionId) {
            return inventoryTransactionRepository.findById(inventoryTransactionId);
        }

        @Transactional
        public InventoryTransaction createInventoryTransaction(InventoryTransaction inventoryTransaction) {
            Product product = productService.getProductById(inventoryTransaction.getProduct().getProductId());
            if(product == null) {
                throw new RuntimeException("Product not found or Create a new Product");
            }
            StorageLocation storageLocation = storageLocationService.getStorageLocationById(inventoryTransaction.getLocation().getLocationId());
            if(storageLocation == null) {
                throw new RuntimeException("Storage location not found or Create a new StorageLocation");
            }

            if(storageLocation.getCapacity() < storageLocation.getOccupiedSpace() + inventoryTransaction.getQuantity()){
                throw new RuntimeException("Space not enough to store the Product");
            }


            product.setQuantityAvailable(product.getQuantityAvailable() + inventoryTransaction.getQuantity());

            productService.updateProduct(product);

            storageLocation.setOccupiedSpace(storageLocation.getOccupiedSpace() + inventoryTransaction.getQuantity());
            storageLocationService.updateStorageLocation(storageLocation);

            inventoryTransaction.setTransactionDate(Instant.now());
            inventoryTransaction.setTransactionType("Shipped");
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
