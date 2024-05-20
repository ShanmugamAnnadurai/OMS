package org.freshkart.oms.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.freshkart.oms.entity.Customer;
import org.freshkart.oms.entity.PaymentStatus;
import org.freshkart.oms.repository.PaymentStatusRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PaymentStatusService {
    @Inject
    PaymentStatusRepository paymentStatusRepository;

    public List<PaymentStatus> getAllPaymentStatus() {
        return paymentStatusRepository.listAll();
    }

    public PaymentStatus getPaymentStatusById(Long paymentStatusId) {
        return paymentStatusRepository.findById(paymentStatusId);
    }

    @Transactional
    public PaymentStatus createPaymentStatus(PaymentStatus paymentStatus) {
        paymentStatusRepository.persist(paymentStatus);
        return paymentStatus;

    }

    @Transactional
    public PaymentStatus updatePaymentStatus(PaymentStatus updatedPaymentStatus) {
        Optional<PaymentStatus> existingPaymentStatus = Optional.ofNullable(getPaymentStatusById(updatedPaymentStatus.getPaymentId()));
        if (existingPaymentStatus.isPresent()) {
            return paymentStatusRepository.getEntityManager().merge(updatedPaymentStatus);
        } else {
            throw new IllegalArgumentException("PaymentStatus with ID " + updatedPaymentStatus.getPaymentId() + " does not exist");
        }
    }

    @Transactional
    public boolean deletePaymentStatus(Long paymentStatusId) {
        return paymentStatusRepository.deleteById(paymentStatusId);
    }
}
