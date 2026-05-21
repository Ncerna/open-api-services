package org.adminBo.dto.payment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PurchaseItemDTO {
    private String name;
    private double price;
    private int quantity;
    private double subtotal;
}
