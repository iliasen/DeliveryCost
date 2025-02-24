package com.iliasen.delivcost.specification;

import com.iliasen.delivcost.models.Partner;
import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.models.TransportType;
import org.springframework.data.jpa.domain.Specification;

public class TransportSpecification {

    public static Specification<Transport> hasTransportType(TransportType transportType) {
        return (root, query, builder) -> builder.equal(root.get("transportType"), transportType);
    }

    public static Specification<Transport> hasTonnageGreaterThan(double tonnage) {
        return (root, query, builder) -> builder.greaterThan(root.get("tonnage"), tonnage);
    }

    public static Specification<Transport> hasVolumeGreaterThan(double volume) {
        return (root, query, builder) -> builder.greaterThan(root.get("volume"), volume);
    }

    public static Specification<Transport> hasPartner(Partner partner) {
        return (root, query, builder) -> builder.equal(root.get("partner"), partner);
    }

    public static Specification<Transport> orderByTonnage(boolean asc) {
        return (root, query, builder) -> {
            if (asc) {
                query.orderBy(builder.asc(root.get("tonnage")));
            } else {
                query.orderBy(builder.desc(root.get("tonnage")));
            }
            return null;
        };
    }
}
