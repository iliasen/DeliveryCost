package com.iliasen.delivcost.specification;

import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.models.TransportType;
import org.springframework.data.jpa.domain.Specification;

public class TransportSpecification {

    public static Specification<Transport> belongsToPartner(Long partnerId) {
        return (root, query, criteriaBuilder) -> {
            if (partnerId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("partner").get("id"), partnerId);
        };
    }

    public static Specification<Transport> hasTransportType(TransportType transportType) {
        return (root, query, criteriaBuilder) -> {
            if (transportType == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("transportType"), transportType);
        };
    }


    public static Specification<Transport> hasDriver() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("driver"));
    }

    public static Specification<Transport> uniqueTransportTypes() {
        return (root, query, criteriaBuilder) -> {
            query.groupBy(root.get("transportType"));
            return criteriaBuilder.conjunction();
        };
    }
}

