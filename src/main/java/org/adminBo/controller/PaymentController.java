package org.adminBo.controller;

import org.adminBo.contact.ICheckoutService;
import org.adminBo.contact.IPayPalService;
import org.adminBo.contact.IMercadoPagoService;
import org.adminBo.dto.payment.MercadoPagoPreferenceDTO;
import org.adminBo.dto.payment.PayPalPaymentDTO;
import org.adminBo.dto.payment.WebhookDTO;
import org.adminBo.wrapper.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final IPayPalService payPalService;
    private final IMercadoPagoService mercadoPagoService;
    private final ICheckoutService checkoutService;

    public PaymentController(IPayPalService payPal,
                             IMercadoPagoService mercadoPago,
                             ICheckoutService checkout ) {
        this.payPalService = payPal;
        this.mercadoPagoService = mercadoPago;
        this.checkoutService = checkout;
    }

    @PostMapping("/payment-pp")
    public ApiResponse<String> processPayPalPayment(@RequestBody PayPalPaymentDTO dto) {
        return payPalService.createSale(dto);
    }

    @PostMapping("/payment-pp/refund/{paypalId}")
    public ApiResponse<String> refundPayPalPayment(@PathVariable String paypalId) {
        return payPalService.refundSale(paypalId);
    }
    @PostMapping("/payment-mp-create-preference")
    public ApiResponse<String> createMercadoPagoPreference(
            @RequestBody MercadoPagoPreferenceDTO dto) {

        return checkoutService.createPreference(dto);
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
