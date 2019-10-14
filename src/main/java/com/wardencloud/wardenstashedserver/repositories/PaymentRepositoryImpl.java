package com.wardencloud.wardenstashedserver.repositories;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import com.wardencloud.wardenstashedserver.entities.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class PaymentRepositoryImpl implements PaymentRepository {
    @Autowired
    EntityManager entityManager;

    @Override
    public Payment findById(int id) {
        return entityManager.find(Payment.class, id);
    }

    @Override
    public Payment addPayment(Payment payment) {
        entityManager.persist(payment);
        entityManager.flush();
        entityManager.refresh(payment);
        return payment;
    }
}
