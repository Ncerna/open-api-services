package org.adminBo.contact;

import org.adminBo.dto.payment.PayPalPaymentDTO;
import org.adminBo.wrapper.ApiResponse;

public interface IPayPalService {
    ApiResponse<String> createSale(PayPalPaymentDTO dto);

    ApiResponse<String> refundSale(String paypalId);
}
