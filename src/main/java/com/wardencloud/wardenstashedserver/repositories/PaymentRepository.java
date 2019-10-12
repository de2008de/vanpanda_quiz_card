package com.wardencloud.wardenstashedserver.repositories;

import com.wardencloud.wardenstashedserver.entities.Payment;

public interface PaymentRepository {
    Payment findById(int id);
    Payment addPayment(Payment payment);
}
