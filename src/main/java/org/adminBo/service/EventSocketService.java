package org.adminBo.service;

import org.adminBo.contact.IEventSocketService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventSocketService implements IEventSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public EventSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void emit(String channel, Object data) {
        messagingTemplate.convertAndSend("/topic/" + channel, data);
        System.out.println("Evento emitido al canal: " + channel);
    }
}
