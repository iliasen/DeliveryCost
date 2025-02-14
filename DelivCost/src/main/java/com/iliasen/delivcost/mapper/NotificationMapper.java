package com.iliasen.delivcost.mapper;

import com.iliasen.delivcost.dto.NotificationDTO;
import com.iliasen.delivcost.models.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "partner.id", target = "partnerId")
    @Mapping(source = "client.id", target = "clientId")
    NotificationDTO toNotificationDTO(Notification notification);

    @Mapping(source = "orderId", target = "order.id")
    @Mapping(source = "partnerId", target = "partner.id")
    @Mapping(source = "clientId", target = "client.id")
    Notification toNotification(NotificationDTO notificationDTO);
}

