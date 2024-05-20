package org.freshkart.oms.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.freshkart.oms.entity.PurchaseOrderItem;
import org.freshkart.oms.repository.PurchaseOrderItemRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PurchaseOrderItemService {
    @Inject
    PurchaseOrderItemRepository purchaseOrderItemRepository;

    public List<PurchaseOrderItem> getAllPurchaseOrderItem() {
        return purchaseOrderItemRepository.listAll();
    }

    public PurchaseOrderItem getPurchaseOrderItemById(Long purchaseOrderItemId) {
        return purchaseOrderItemRepository.findById(purchaseOrderItemId);
    }

    @Transactional
    public PurchaseOrderItem createPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        purchaseOrderItemRepository.persist(purchaseOrderItem);
        return purchaseOrderItem;
    }

    @Transactional
    public PurchaseOrderItem updatePurchaseOrderItem(PurchaseOrderItem updatedPurchaseOrderItem) {
        Optional<PurchaseOrderItem> existingPurchaseOrderItem = Optional.ofNullable(getPurchaseOrderItemById(updatedPurchaseOrderItem.getOrderDetailsId()));
        if (existingPurchaseOrderItem.isPresent()) {
            return purchaseOrderItemRepository.getEntityManager().merge(updatedPurchaseOrderItem);
        } else {
            throw new IllegalArgumentException("Purchase Order Item with ID " +  updatedPurchaseOrderItem.getOrderDetailsId() + " does not exist");
        }
    }

    @Transactional
    public boolean deletePurchaseOrderItem(Long purchaseOrderItemId) {
        return purchaseOrderItemRepository.deleteById(purchaseOrderItemId);
    }
}
