package com.wardencloud.wardenstashedserver.services;

import java.util.Map;

import com.wardencloud.wardenstashedserver.entities.Payment;

public interface PaymentService {
    Map<String, Object> processPayment(int userId, Payment payment);
}
