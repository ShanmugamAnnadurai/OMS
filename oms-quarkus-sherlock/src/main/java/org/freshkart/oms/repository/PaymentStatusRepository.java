package org.freshkart.oms.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.freshkart.oms.entity.PaymentStatus;

@ApplicationScoped
public class PaymentStatusRepository implements PanacheRepository<PaymentStatus> {
}
