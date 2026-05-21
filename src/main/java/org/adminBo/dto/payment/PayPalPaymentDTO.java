package org.adminBo.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayPalPaymentDTO {
    @JsonProperty("paypal_id")
    private String paypalId;
    private PayPalPayerDTO payer;
    private List<PayPalItemDTO> items;
    private Double total;
    private String currency;
    private String status;
    private String date;
}
