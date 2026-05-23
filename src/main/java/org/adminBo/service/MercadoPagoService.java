package org.adminBo.service;
import jakarta.transaction.Transactional;
import org.adminBo.contact.*;
import org.adminBo.dto.payment.*;
import org.adminBo.entity.*;
import org.adminBo.repository.*;
import org.adminBo.utils.*;
import org.adminBo.wrapper.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class MercadoPagoService implements IMercadoPagoService {

    private final ISaleService saleService;
    private final IClientService clientService;
    private final INotificationService notification;
    private final IEventSocketService socketService;
    private static final Logger log = LoggerFactory.getLogger(MercadoPagoService.class);

    public MercadoPagoService(
            ISaleService saleService,
            IClientService clientService,
            @Qualifier("emailNotificationService")
            INotificationService notification,
            IEventSocketService socketService
    ) {

        this.saleService = saleService;
        this.clientService = clientService;
        this.notification = notification;
        this.socketService = socketService;
    }

    @Override
    @Transactional
    public ApiResponse<String> createSale( WebhookDTO dto) {

        boolean exists = saleService.transactionExists(dto.id.toString());

        if (exists)  return ApiResponse.success( "Sale already exists", null);

        Client client = clientService.findOrCreate( dto.payer.email, PaymentMapper.mapClient(dto));

        Sale sale =  PaymentMapper.mapDtoToSale(dto);

        sale.setPaypalId(sale.getOrderId());

        sale.setClient(client);

        saleService.save(sale);

        PurchaseDTO purchase = PaymentMapper.mapToPurchaseDTO(dto);

        notification.send(client.getEmail(), purchase);

        String channel = UserChannelUtil.getChannel( dto.external_reference );

        socketService.emit(channel, sale);

        return ApiResponse.success( "MercadoPago sale created successfully", sale.getOrderId());
    }

    @Override
    @Transactional
    public ApiResponse<String> refundSale(  String paymentId) {
        saleService.refund(paymentId);
        return ApiResponse.success( "MercadoPago refund success",  paymentId);
    }
}