package com.iliasen.delivcost.specification;

import com.iliasen.delivcost.models.Order;
import com.iliasen.delivcost.models.OrderStatus;
import com.iliasen.delivcost.models.TransportType;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {

    public static Specification<Order> hasStatus(OrderStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("orderStatus"), status);
        };
    }

    public static Specification<Order> belongsToPartner(Long partnerId) {
        return (root, query, criteriaBuilder) -> {
            if (partnerId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("partner").get("id"), partnerId);
        };
    }

    public static Specification<Order> belongsToClient(Long clientId) {
        return (root, query, criteriaBuilder) -> {
            if (clientId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("client").get("id"), clientId);
        };
    }

    public static Specification<Order> assignedToDriver(Long driverId) {
        return (root, query, criteriaBuilder) -> {
            if (driverId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("driver").get("id"), driverId);
        };
    }

    public static Specification<Order> hasTransportType(TransportType transportType) {
        return (root, query, criteriaBuilder) -> {
            if (transportType == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("route").get("transportType"), transportType);
        };
    }

    public static Specification<Order> isNotComplete() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("orderStatus"), OrderStatus.COMPLETE);
    }

    public static Specification<Order> hasNoDriver() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("driver"));
    }
}
