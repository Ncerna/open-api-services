package org.adminBo.contact;

public interface IEventSocketService {

    void emit(  String channel,  Object data);

}