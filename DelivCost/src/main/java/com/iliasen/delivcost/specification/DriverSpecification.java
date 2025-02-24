package com.iliasen.delivcost.specification;

import com.iliasen.delivcost.models.Driver;
import com.iliasen.delivcost.models.Role;
import org.springframework.data.jpa.domain.Specification;

public class DriverSpecification {

    public static Specification<Driver> hasFirstName(String firstName) {
        return (root, query, builder) -> builder.equal(root.get("firstName"), firstName);
    }

    public static Specification<Driver> hasLastName(String lastName) {
        return (root, query, builder) -> builder.equal(root.get("lastName"), lastName);
    }

    public static Specification<Driver> hasPhone(String phone) {
        return (root, query, builder) -> builder.equal(root.get("phone"), phone);
    }

    public static Specification<Driver> hasEmail(String email) {
        return (root, query, builder) -> builder.equal(root.get("email"), email);
    }

    public static Specification<Driver> hasRole(Role role) {
        return (root, query, builder) -> builder.equal(root.get("role"), role);
    }

    public static Specification<Driver> orderByFirstName(boolean asc) {
        return (root, query, builder) -> {
            if (asc) {
                query.orderBy(builder.asc(root.get("firstName")));
            } else {
                query.orderBy(builder.desc(root.get("firstName")));
            }
            return null;
        };
    }

    // Добавьте другие спецификации при необходимости
}
