package org.adminBo.service;

import com.mercadopago.client.preference.*;
import com.mercadopago.resources.preference.Preference;
import org.adminBo.contact.ICheckoutService;
import org.adminBo.dto.payment.CartItemDTO;
import org.adminBo.dto.payment.MercadoPagoPreferenceDTO;
import org.adminBo.wrapper.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CheckoutService implements ICheckoutService {
    private static final Logger log = LoggerFactory.getLogger(CheckoutService.class);

    @Override
    public ApiResponse<String> createPreference( MercadoPagoPreferenceDTO dto) {
        try {
            // 2. items MP
            List<PreferenceItemRequest> items =
                    dto.getCart()
                            .stream()
                            .map(this::mapToItem)
                            .toList();

            // 3. payer
            PreferencePayerRequest payer =
                    PreferencePayerRequest.builder()
                            .name(dto.getUsername())
                            .email(dto.getEmail())
                            .build();
            // 4. back urls
            PreferenceBackUrlsRequest backUrls =
                    PreferenceBackUrlsRequest.builder()
                            .success("https://frontend.com/success")
                            .failure("https://frontend.com/failure")
                            .pending("https://frontend.com/pending")
                            .build();
            // 5. request
            PreferenceRequest preferenceRequest =
                    PreferenceRequest.builder()
                            .items(items)
                            .payer(payer)
                            .externalReference( generateExternalReference(dto.getUsername()))
                            .statementDescriptor("")
                            .notificationUrl(
                                    "https://api.com/api/payments/payment-mp-webhook"
                            )
                            .backUrls(backUrls)
                            .build();
            PreferenceClient client = new PreferenceClient();
            Preference preference =  client.create(preferenceRequest);
            return ApiResponse.success(
                    preference.getId()
            );

        } catch (Exception e) {

            return ApiResponse.error(500, e.getMessage());
        }
    }
    private PreferenceItemRequest mapToItem(CartItemDTO item) {

        return PreferenceItemRequest.builder()
                .id(String.valueOf(item.getId()))
                .title(item.getName())
                .pictureUrl(item.getImage())
                .quantity(item.getQuantity())
                .currencyId("PEN")
                .unitPrice(item.getPrice())
                .build();
    }
    public static String generateExternalReference(String username) {
        return "MP_" + username + "_" + java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
