package org.freshkart.ims.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.freshkart.ims.entity.Shipment;
import org.freshkart.ims.repository.ShipmentRepository;
import org.freshkart.oms.service.PurchaseOrderService;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ShipmentService {
    @Inject
    ShipmentRepository shipmentRepository;

    @Inject
    PurchaseOrderService purchaseOrderService;

    public List<Shipment> getAllShipment() {
        return shipmentRepository.listAll();
    }

    public Shipment getShipmentById(Long shipmentId) {
        return shipmentRepository.findById(shipmentId);
    }

    @Transactional
    public Shipment createShipment(Shipment shipment) {
        shipment.setShipmentDate(Instant.now());
        shipment.setStatus("Shipped");
        shipmentRepository.persist(shipment);
        return shipment;
    }

    @Transactional
    public Shipment updateShipment(Shipment updatedShipment) {
        Optional<Shipment> existingShipment = Optional.ofNullable(getShipmentById(updatedShipment.getShipmentId()));
        if (existingShipment.isPresent()) {
            return shipmentRepository.getEntityManager().merge(updatedShipment);
        } else {
            throw new IllegalArgumentException("Shipment with ID " + updatedShipment.getShipmentId() + " does not exist");
        }
    }


    @Transactional
    public Shipment updateShipmentForDelivery(Shipment updatedShipment) {
        Optional<Shipment> existingShipment = Optional.ofNullable(getShipmentById(updatedShipment.getShipmentId()));
        if (existingShipment.isPresent()) {
            updatedShipment.setArrivalDate(Instant.now());
            updatedShipment.setStatus("Delivered");
            if(updatedShipment.getArrivalDate() != null){
                purchaseOrderService.updatePurchaseOrderStatus(updatedShipment.getPurchaseOrder().getPurchaseOrderId(),"Delivered");
            }
            return shipmentRepository.getEntityManager().merge(updatedShipment);
        } else {
            throw new IllegalArgumentException("Shipment with ID " + updatedShipment.getShipmentId() + " does not exist");
        }


    }

    @Transactional
    public boolean deleteShipment(Long shipmentId) {
        return shipmentRepository.deleteById(shipmentId);
    }
}
