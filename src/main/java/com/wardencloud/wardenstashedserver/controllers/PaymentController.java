package com.wardencloud.wardenstashedserver.controllers;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.wardencloud.wardenstashedserver.entities.Payment;
import com.wardencloud.wardenstashedserver.entities.User;
import com.wardencloud.wardenstashedserver.services.PaymentService;
import com.wardencloud.wardenstashedserver.services.TokenService;
import com.wardencloud.wardenstashedserver.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/payment")
public class PaymentController {

    @Autowired
    @Qualifier("paymentServiceImpl")
    private PaymentService paymentService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity processPayment(@RequestHeader("token") String token, @RequestBody Payment payment) {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();
        int userId = tokenService.getUserIdFromToken(token);
        Map<String, Object> result = paymentService.processPayment(userId, payment);
        if ((int) result.get("status") == -1) {
            jsonObject.put("error", "Failed to add credit");
            return ResponseEntity.badRequest().body(jsonObject);
        }
        User user = userService.findUserById(userId);
        data.put("credit", result.get("credit"));
        data.put("company", "Vanpanda");
        data.put("amount", payment.getAmount());
        data.put("username", user.getUsername());
        data.put("invoiceId", result.get("paymentId"));
        jsonObject.put("data", data);
        return ResponseEntity.ok().body(jsonObject);
    }
}
