package org.freshkart.wms.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.freshkart.wms.entity.InventoryTransaction;

@ApplicationScoped
public class InventoryTransactionRepository implements PanacheRepository<InventoryTransaction> {
}