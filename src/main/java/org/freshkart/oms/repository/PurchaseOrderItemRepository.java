package org.freshkart.oms.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.freshkart.oms.entity.PurchaseOrderItem;


@ApplicationScoped
public class PurchaseOrderItemRepository implements PanacheRepository<PurchaseOrderItem> {
}