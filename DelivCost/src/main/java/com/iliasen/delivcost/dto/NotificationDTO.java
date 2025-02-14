package com.iliasen.delivcost.dto;

import com.iliasen.delivcost.models.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private OrderStatus newStatus;
    private boolean partnerChecked;
    private boolean clientChecked;
    private LocalDateTime changeTime;
    private Long orderId;
    private Long partnerId;
    private Long clientId;
}
