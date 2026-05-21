package org.adminBo.repository;


import org.adminBo.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesRepository  extends JpaRepository<Sale, Long> {
    Optional<Sale> findByPaypalId(String paypalId);
    Optional<Sale> findByOrderId(String orderId);

}