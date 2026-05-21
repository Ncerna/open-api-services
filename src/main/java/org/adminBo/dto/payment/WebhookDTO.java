package org.adminBo.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookDTO {
    public Long id;
    public String status;
    public String status_detail;
    public Double transaction_amount;
    public String currency_id;
    public String payment_method_id;
    public String external_reference;
    public String authorization_code;
    public Boolean captured;
    public Integer installments;
    public String description;
    public String date_created;
    public String date_approved;


    public Payer payer;
    public Card card;


    public Additional_info additional_info;

    public static class Payer {
        public String id;
        public String email;
    }

    public static class Card {
        public String country;
    }

    public static class Additional_info {
        public List<Item> items;

        public static class Item {
            public String id;
            public String title;
            public Integer quantity;
            public Double unit_price;
            public String picture_url;
        }
    }
}
