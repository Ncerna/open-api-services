package org.adminBo.service.strategy;

import org.adminBo.contact.INotificationService;
import org.adminBo.dto.payment.PurchaseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("smsNotificationService")
public class SmsNotificationService
        implements INotificationService {

    @Override
    public void send( String to, PurchaseDTO purchase) {

        System.out.println("SMS SENT");

        System.out.println("PHONE: " + to);

        System.out.println("MESSAGE: " );
    }
}
