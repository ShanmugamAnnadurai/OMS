package org.freshkart.oms.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.freshkart.oms.entity.Customer;
import org.freshkart.oms.entity.PurchaseOrder;
import org.freshkart.oms.repository.CustomerRepository;
import org.freshkart.oms.repository.PurchaseOrderRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PurchaseOrderService {
    @Inject
    PurchaseOrderRepository purchaseOrderRepository;

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.listAll();
    }

    @Transactional
    public PurchaseOrder getPurchaseOrderById(Long purchaseOrderId) {
        return purchaseOrderRepository.findById(purchaseOrderId);
    }

    @Transactional
    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        purchaseOrder.setStatus("Pending");
        purchaseOrderRepository.persist(purchaseOrder);
        return purchaseOrder;

    }

    @Transactional
    public void updatePurchaseOrderStatus(Long purchaseOrderId, String shipmentStatus) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderId);
        if (purchaseOrder != null) {
            if (shipmentStatus.equals("Shipped")) {
                purchaseOrder.setStatus("Shipped");
            } else if (shipmentStatus.equals("Delivered")) {
                purchaseOrder.setStatus("Delivered");
                purchaseOrder.setDeliveryDate(Instant.now());
            }
            // Update the purchase order status
            updatePurchaseOrder(purchaseOrder);
        }
    }



    @Transactional
    public PurchaseOrder updatePurchaseOrder(PurchaseOrder updatedPurchaseOrder) {
        Optional<PurchaseOrder> existingPurchaseOrder = Optional.ofNullable(getPurchaseOrderById(updatedPurchaseOrder.getPurchaseOrderId()));
        if (existingPurchaseOrder.isPresent()) {
            return purchaseOrderRepository.getEntityManager().merge(updatedPurchaseOrder);
        } else {
            throw new IllegalArgumentException("Purchase Order with ID " + updatedPurchaseOrder.getPurchaseOrderId() + " does not exist");
        }
    }

    @Transactional
    public boolean deletePurchaseOrder(Long purchaseOrderId) {
        return purchaseOrderRepository.deleteById(purchaseOrderId);
    }
}
