package org.adminBo.service;

import jakarta.transaction.Transactional;
import org.adminBo.contact.*;
import org.adminBo.dto.payment.PayPalPaymentDTO;
import org.adminBo.dto.payment.PurchaseDTO;
import org.adminBo.entity.Client;
import org.adminBo.entity.Sale;
import org.adminBo.repository.*;
import org.adminBo.utils.PaymentMapper;
import org.adminBo.wrapper.ApiResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PayPalService
        implements IPayPalService {

    private final ISaleService saleService;

    private final IClientService clientService;

    private final INotificationService notification;

    public PayPalService(

            ISaleService saleService,
            IClientService clientService,
            @Qualifier("emailNotificationService")
            INotificationService notification
    ) {
        this.saleService = saleService;
        this.clientService = clientService;
        this.notification = notification;
    }

    @Override
    @Transactional
    public ApiResponse<String> createSale(PayPalPaymentDTO dto) {
        boolean exists = saleService.transactionExists(dto.getPaypalId());
        if (exists) return ApiResponse.success(  "Sale already exists",  null  );
        Client client = clientService.findOrCreate( dto.getPayer().getEmail(),PaymentMapper.mapDtoToClient(dto));
        Sale sale = PaymentMapper.mapDtoToSale(dto,client);
        Sale savedSale = saleService.save(sale);
        PurchaseDTO purchase = PaymentMapper.mapToPurchaseDTO(dto);
        notification.send( client.getEmail(), purchase);
        return ApiResponse.success( "PayPal sale created successfully",  savedSale.getPaypalId());
    }

    @Override
    @Transactional
    public ApiResponse<String> refundSale( String paymentId) {
        saleService.refund(paymentId);
        return ApiResponse.success( "PayPal refund success",  paymentId);
    }
}