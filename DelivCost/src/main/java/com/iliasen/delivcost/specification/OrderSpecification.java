package com.iliasen.delivcost.specification;

import com.iliasen.delivcost.models.Order;
import com.iliasen.delivcost.models.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {

    public static Specification<Order> hasComment(String comment) {
        return (root, query, builder) -> builder.equal(root.get("comment"), comment);
    }

    public static Specification<Order> hasOrderStatus(OrderStatus orderStatus) {
        return (root, query, builder) -> builder.equal(root.get("orderStatus"), orderStatus);
    }

        public static Specification<Order> hasPriceGreaterThan(int price) {
        return (root, query, builder) -> builder.greaterThan(root.get("price"), price);
    }

    public static Specification<Order> isPartnerChecked(boolean partnerChecked) {
        return (root, query, builder) -> builder.equal(root.get("partnerChecked"), partnerChecked);
    }

    public static Specification<Order> orderByPrice(boolean asc) {
        return (root, query, builder) -> {
            if (asc) {
                query.orderBy(builder.asc(root.get("price")));
            } else {
                query.orderBy(builder.desc(root.get("price")));
            }
            return null;
        };
    }
}