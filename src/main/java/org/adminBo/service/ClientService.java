package org.adminBo.service;

import org.adminBo.contact.IClientService;
import org.adminBo.dto.payment.WebhookDTO;
import org.adminBo.entity.Client;
import org.adminBo.repository.ClientRepository;
import org.adminBo.utils.PaymentMapper;
import org.springframework.stereotype.Service;


@Service
public class ClientService
        implements IClientService {

    private final ClientRepository repository;

    public ClientService(
            ClientRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public Client findByEmail(String email) {
        return repository.findByEmail(email)
                .orElse(null);
    }
    @Override
    public Client save(Client client) {

        return repository.save(client);
    }
    @Override
    public Client findOrCreate(
            String email,
            Client client
    ) {
        return repository.findByEmail(email)
                .orElseGet(() ->
                        repository.save(client)
                );
    }
}
