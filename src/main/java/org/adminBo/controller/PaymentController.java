package org.adminBo.controller;

import org.adminBo.contact.IPayPalService;
import org.adminBo.contact.IMercadoPagoService;
import org.adminBo.dto.payment.PayPalPaymentDTO;
import org.adminBo.dto.payment.WebhookDTO;
import org.adminBo.wrapper.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final IPayPalService payPalService;
    private final IMercadoPagoService mercadoPagoService;

    public PaymentController(IPayPalService payPalService,
                             IMercadoPagoService mercadoPagoService) {
        this.payPalService = payPalService;
        this.mercadoPagoService = mercadoPagoService;
    }

    @PostMapping("/payment-pp")
    public ApiResponse<String> processPayPalPayment(@RequestBody PayPalPaymentDTO dto) {
        return payPalService.createSale(dto);
    }

    @PostMapping("/payment-pp/refund/{paypalId}")
    public ApiResponse<String> refundPayPalPayment(@PathVariable String paypalId) {
        return payPalService.refundSale(paypalId);
    }

    @PostMapping("/payment-webhook")
    public ApiResponse<String> processMercadoPagoPayment(@RequestBody WebhookDTO mpDto) {
        return mercadoPagoService.createSale(mpDto);
    }

    @PostMapping("/payment-mc/refund/{mpId}")
    public ApiResponse<String> refundMercadoPagoPayment(@PathVariable String mpId) {
        return mercadoPagoService.refundSale(mpId);
    }
}
