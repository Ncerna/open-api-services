package org.adminBo.contact;

import org.adminBo.entity.Sale;
public interface ISaleService {
    Sale save(Sale sale);
    void refund(String paymentId);
    boolean transactionExists(String trx);
}
