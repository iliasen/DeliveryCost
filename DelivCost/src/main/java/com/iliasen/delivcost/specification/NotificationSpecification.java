package com.iliasen.delivcost.specification;

import com.iliasen.delivcost.models.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class NotificationSpecification {

    public static Specification<Notification> hasNewStatus(OrderStatus newStatus) {
        return (root, query, builder) -> builder.equal(root.get("newStatus"), newStatus);
    }

    public static Specification<Notification> isPartnerChecked(boolean partnerChecked) {
        return (root, query, builder) -> builder.equal(root.get("partnerChecked"), partnerChecked);
    }

    public static Specification<Notification> isClientChecked(boolean clientChecked) {
        return (root, query, builder) -> builder.equal(root.get("clientChecked"), clientChecked);
    }

    public static Specification<Notification> hasChangeTimeAfter(LocalDateTime changeTime) {
        return (root, query, builder) -> builder.greaterThan(root.get("changeTime"), changeTime);
    }

    public static Specification<Notification> hasOrder(Order order) {
        return (root, query, builder) -> builder.equal(root.get("order"), order);
    }

    public static Specification<Notification> hasPartner(Partner partner) {
        return (root, query, builder) -> builder.equal(root.get("partner"), partner);
    }

    public static Specification<Notification> hasClient(Client client) {
        return (root, query, builder) -> builder.equal(root.get("client"), client);
    }

    public static Specification<Notification> orderByChangeTime(boolean asc) {
        return (root, query, builder) -> {
            if (asc) {
                query.orderBy(builder.asc(root.get("changeTime")));
            } else {
                query.orderBy(builder.desc(root.get("changeTime")));
            }
            return null;
        };
    }
}
