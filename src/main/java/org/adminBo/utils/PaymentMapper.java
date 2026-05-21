package org.adminBo.utils;

import org.adminBo.dto.payment.*;
import org.adminBo.entity.Client;
import org.adminBo.entity.PaymentMethod;
import org.adminBo.entity.Sale;
import org.adminBo.entity.SalesDetail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentMapper {


    public static Client mapDtoToClient(PayPalPaymentDTO dto) {
        Client client = new Client();
        client.setName(dto.getPayer().getName());
        client.setEmail(dto.getPayer().getEmail());
        client.setCountry(dto.getPayer().getCountry());
        return client;
    }


    public static Sale mapDtoToSale(PayPalPaymentDTO dto, Client client) {
        Sale sale = new Sale();
        sale.setPaypalId(dto.getPaypalId());
        sale.setTotal(dto.getTotal());
        sale.setCurrency(dto.getCurrency());
        sale.setStatus(dto.getStatus());
        sale.setClient(client);

        LocalDateTime date = LocalDateTime.parse(dto.getDate(), DateTimeFormatter.ISO_DATE_TIME);
        sale.setDate(date);

        List<SalesDetail> details = new ArrayList<>();
        for (PayPalItemDTO item : dto.getItems()) {
            SalesDetail detail = new SalesDetail();
            detail.setItemName(item.getName());
            detail.setPrice(item.getPrice());
            detail.setQuantity(item.getQuantity());
            detail.setSubtotal(item.getSubtotal());
            detail.setSale(sale);
            details.add(detail);
        }
        sale.setDetails(details);

        return sale;
    }

    public static Sale mapDtoToSale(WebhookDTO mpDto) {
        Sale sale = mapSale(mpDto);
        Client client = mapClient(mpDto);
        sale.setClient(client);

        List<SalesDetail> details = mapSaleDetails(mpDto, sale);
        sale.setDetails(details);

        return sale;
    }
    public static Client mapClient(WebhookDTO mpDto) {
        Client client = new Client();
        client.setName(mpDto.payer.id); // No hay nombre, usamos id
        client.setEmail(mpDto.payer.email);
        client.setCountry(mpDto.card != null ? mpDto.card.country : null);
        return client;
    }

    private static Sale mapSale(WebhookDTO mpDto) {
        Sale sale = new Sale();
        sale.setOrderId(mpDto.id.toString());
        sale.setTotal(mpDto.transaction_amount);
        sale.setCurrency(mpDto.currency_id);
        sale.setStatus(mpDto.status);

        if (mpDto.date_created != null) {
            LocalDateTime date = LocalDateTime.parse(mpDto.date_created, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            sale.setDate(date);
        }

        return sale;
    }
    private static List<SalesDetail> mapSaleDetails(WebhookDTO mpDto, Sale sale) {
        List<SalesDetail> details = new ArrayList<>();
        if (mpDto.additional_info != null && mpDto.additional_info.items != null) {
            for (WebhookDTO.Additional_info.Item itemDto : mpDto.additional_info.items) {
                SalesDetail detail = new SalesDetail();
                detail.setItemName(itemDto.title);
                detail.setPrice(itemDto.unit_price);
                detail.setQuantity(itemDto.quantity);
                detail.setSubtotal(itemDto.unit_price * itemDto.quantity);
                detail.setSale(sale);
                details.add(detail);
            }
        }
        return details;
    }

    public static PurchaseDTO mapToPurchaseDTO(WebhookDTO dto) {
        List<PurchaseItemDTO> items = dto.additional_info.items.stream()
                .map(item -> {
                    PurchaseItemDTO i = new PurchaseItemDTO();
                    i.setName(item.title);
                    i.setPrice(item.unit_price);
                    i.setQuantity(item.quantity);
                    i.setSubtotal(item.quantity*item.unit_price);
                    return i;
                }).collect(Collectors.toList());

        PurchaseDTO purchase = new PurchaseDTO();
        purchase.setTotal(dto.getTransaction_amount());
        purchase.setCurrency(dto.currency_id);
        purchase.setDate( DateUtil.parseDate(dto.date_approved));
        purchase.setPaymentMethod(PaymentMethod.MERCADO_PAGO);
        purchase.setItems(items);

        return purchase;
    }
    public static PurchaseDTO mapToPurchaseDTO( PayPalPaymentDTO dto) {

        List<PurchaseItemDTO> items = dto.getItems()
                .stream()
                .map(item -> {
                    PurchaseItemDTO purchaseItem = new PurchaseItemDTO();
                    purchaseItem.setName(item.getName());
                    purchaseItem.setPrice(item.getPrice());
                    purchaseItem.setQuantity(item.getQuantity());
                    purchaseItem.setSubtotal( item.getQuantity() * item.getPrice());
                    return purchaseItem;

                }).toList();

        PurchaseDTO purchase = new PurchaseDTO();
        purchase.setTotal(dto.getTotal());
        purchase.setCurrency(dto.getCurrency());
        purchase.setDate(DateUtil.parseDate(dto.getDate()));
        purchase.setPaymentMethod(PaymentMethod.PAYPAL);
        purchase.setItems(items);
        return purchase;
    }



}
