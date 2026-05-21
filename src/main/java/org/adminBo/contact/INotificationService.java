package org.adminBo.contact;

import org.adminBo.dto.payment.PurchaseDTO;

public interface INotificationService {

    void send(String to, PurchaseDTO purchase);

}
