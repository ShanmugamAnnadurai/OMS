package org.freshkart.ims.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.freshkart.ims.entity.ShipmentDetail;
import org.freshkart.ims.repository.ShipmentDetailRepository;


import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ShipmentDetailService {

    @Inject
    ShipmentDetailRepository shipmentDetailRepository;

    public List<ShipmentDetail> getAllShipmentDetail() {
        return shipmentDetailRepository.listAll();
    }

    public ShipmentDetail getShipmentDetailById(Long shipmentDetailId) {
        return shipmentDetailRepository.findById(shipmentDetailId);
    }

    @Transactional
    public ShipmentDetail createShipmentDetail(ShipmentDetail shipmentDetail) {
        shipmentDetailRepository.persist(shipmentDetail);
        return shipmentDetail;
    }

    @Transactional
    public ShipmentDetail updateShipmentDetail(ShipmentDetail updatedShipmentDetail) {
        Optional<ShipmentDetail> existingShipmentDetail = Optional.ofNullable(getShipmentDetailById(updatedShipmentDetail.getShipmentDetailsId()));
        if (existingShipmentDetail.isPresent()) {
            return shipmentDetailRepository.getEntityManager().merge(updatedShipmentDetail);
        } else {
            throw new IllegalArgumentException("ShipmentDetail with ID " + updatedShipmentDetail.getShipmentDetailsId() + " does not exist");
        }
    }

    @Transactional
    public boolean deleteShipmentDetail(Long shipmentDetailId) {
        return shipmentDetailRepository.deleteById(shipmentDetailId);
    }

}
