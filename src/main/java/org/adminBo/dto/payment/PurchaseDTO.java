package org.adminBo.dto.payment;
import lombok.*;
import org.adminBo.entity.PaymentMethod;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDTO {
    private double total;
    private String currency;
    private LocalDateTime date;
    private List<PurchaseItemDTO> items;
    private PaymentMethod paymentMethod;
}