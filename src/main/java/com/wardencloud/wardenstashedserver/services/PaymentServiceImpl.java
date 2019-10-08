package com.wardencloud.wardenstashedserver.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.wardencloud.wardenstashedserver.entities.Payment;
import com.wardencloud.wardenstashedserver.entities.User;
import com.wardencloud.wardenstashedserver.repositories.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Qualifier("paymentServiceImpl")
public class PaymentServiceImpl implements PaymentService {
    @Value("${stripe.secret}")
    private String stripeSecret;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentRepository paymentRepository;

    final private int dollarCreditRatio = 100;
    // Stripe accepts integer only for smallest dollar unit: cent
    // Example: 1.50 => 150
    final private int dollarCentRatio = 100;

    public Map<String, Object> processPayment(int userId, Payment payment) {
        Map<String, Object> result = new HashMap<>();
        User validatedUser = userService.findUserById(userId);
        if (validatedUser == null) {
            result.put("status", -1);
            return result;
        }
        payment.setUserId(userId);
        Stripe.apiKey = stripeSecret;
        int amount = (int) Math.ceil(payment.getAmount().doubleValue() * dollarCentRatio);
        int credit = (int) payment.getAmount().doubleValue() * dollarCreditRatio;
        String stripeToken = payment.getToken();
        Map<String, Object> params = new HashMap<>();
        String description = "Add credit, dollar of " + amount + " cents, " + "credit of " + credit + ", "
                + "for user id " + userId;
        payment.setDescription(description);
        params.put("amount", amount);
        params.put("currency", "cad");
        params.put("description", description);
        params.put("source", stripeToken);
        params.put("statement_descriptor", "Vanpanda credit charge");
        try {
            // If no exception is thrown and Charge object is created,
            // the payment is success
            Charge charge = Charge.create(params);
            Payment addedPayment = paymentRepository.addPayment(payment);
            User user = userService.addCreditForUserById(userId, credit);
            result.put("status", 0);
            result.put("credit", user.getCredit());
            result.put("paymentId", addedPayment.getId());
            return result;
        } catch (StripeException e) {
            result.put("status", -1);
            return result;
        }
    }
}
