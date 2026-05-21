package org.adminBo.contact;
import org.adminBo.entity.Client;
public interface IClientService {
    Client findByEmail(String email);
    Client save(Client client);
    Client findOrCreate(  String email, Client client);
}
