package org.adminBo.entity;

public enum PaymentMethod {

    PAYPAL("PayPal"),
    MERCADO_PAGO("Mercado Pago");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
