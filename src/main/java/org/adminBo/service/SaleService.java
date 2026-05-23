package org.adminBo.service;

import org.adminBo.contact.ISaleService;
import org.adminBo.entity.Sale;
import org.adminBo.repository.SalesRepository;
import org.springframework.stereotype.Service;

@Service
public class SaleService implements ISaleService {
    private final SalesRepository repository;

    public SaleService( SalesRepository repository  ) {
        this.repository = repository;
    }
    @Override
    public Sale save(Sale sale) {
        return repository.save(sale);
    }

    @Override
    public void refund(String paymentId) {
        Sale sale =  repository.findByPaypalId(paymentId)
                .orElseThrow(() -> new RuntimeException( "Sale not found" ));
        sale.setStatus("REFUNDED");
        repository.save(sale);
    }

    @Override
    public boolean transactionExists(String paymentId ) {

        return repository.findByPaypalId(paymentId).isPresent();
    }
}
