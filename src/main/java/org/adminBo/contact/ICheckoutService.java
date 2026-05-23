package org.adminBo.contact;

import org.adminBo.dto.payment.MercadoPagoPreferenceDTO;
import org.adminBo.wrapper.ApiResponse;

public interface ICheckoutService {
    ApiResponse<String> createPreference(MercadoPagoPreferenceDTO dto);
}
