package com.iliasen.delivcost.dto;

import com.iliasen.delivcost.models.OrderStatus;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.iliasen.delivcost.models.Notification}
 */
@Value
public class NotificationDTO {
    Long id;
    OrderStatus newStatus;
    boolean partnerChecked;
    boolean clientChecked;
    LocalDateTime changeTime;
    OrderDTO order;
    PartnerDTO partner;
    ClientDTO client;
}