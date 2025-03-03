package com.iliasen.delivcost.specification;

import com.iliasen.delivcost.models.Partner;
import org.springframework.data.jpa.domain.Specification;

public class PartnerSpecification {
    public static Specification<Partner> hasCompanyOfficialFilled() {
        return (root, query, builder) -> builder.isNotNull(root.get("companyOfficial"));
    }

    public static Specification<Partner> hasDescriptionFilled() {
        return (root, query, builder) -> builder.isNotNull(root.get("description"));
    }

    public static Specification<Partner> hasMarginFilled() {
        return (root, query, builder) -> builder.isNotNull(root.get("margin"));
    }

//    public static Specification<Partner> hasTransport() {
//        return (root, query, builder) -> builder.isNotEmpty(root.get("transports"));
//    }

    public static Specification<Partner> hasTransport() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.isNotEmpty(root.get("transportList"));
        };
    }

    public static Specification<Partner> orderByCompanyName(boolean asc) {
        return (root, query, builder) -> {
            if (asc) {
                query.orderBy(builder.asc(root.get("companyName")));
            } else {
                query.orderBy(builder.desc(root.get("companyName")));
            }
            return null;
        };
    }

    public static Specification<Partner> orderByRating(boolean asc) {
        return (root, query, builder) -> {
            if (asc) {
                query.orderBy(builder.asc(root.get("rating")));
            } else {
                query.orderBy(builder.desc(root.get("rating")));
            }
            return null;
        };
    }
}