package com.iliasen.delivcost.dto.mapper;

import com.iliasen.delivcost.dto.NotificationDTO;
import com.iliasen.delivcost.models.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface NotificationMapper {
    Notification toEntity(NotificationDTO notificationDTO);

    NotificationDTO toNotificationDTO(Notification notification);
}