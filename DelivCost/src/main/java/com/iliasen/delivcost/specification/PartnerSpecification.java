package com.iliasen.delivcost.specification;

import com.iliasen.delivcost.models.Partner;
import com.iliasen.delivcost.models.Role;
import org.springframework.data.jpa.domain.Specification;

public class PartnerSpecification {

    public static Specification<Partner> hasCompanyName(String companyName) {
        return (root, query, builder) -> builder.equal(root.get("companyName"), companyName);
    }

    public static Specification<Partner> hasInn(Long inn) {
        return (root, query, builder) -> builder.equal(root.get("inn"), inn);
    }

    public static Specification<Partner> hasEmail(String email) {
        return (root, query, builder) -> builder.equal(root.get("email"), email);
    }

    public static Specification<Partner> hasRole(Role role) {
        return (root, query, builder) -> builder.equal(root.get("role"), role);
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
}