package org.adminBo.dto.payment;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class CartItemDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String image;
    private Integer quantity;
    private Integer stock;
}
