package org.adminBo.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MercadoPagoPreferenceDTO {
    private BigDecimal total;
    private String username;
    private String email;
    private List<CartItemDTO> cart;
}