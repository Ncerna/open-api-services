package org.adminBo.contact;

import org.adminBo.dto.payment.PayPalPaymentDTO;
import org.adminBo.dto.payment.WebhookDTO;
import org.adminBo.wrapper.ApiResponse;

public interface IMercadoPagoService {
    ApiResponse<String> createSale(WebhookDTO dto);

    ApiResponse<String> refundSale(String paypalId);
}
