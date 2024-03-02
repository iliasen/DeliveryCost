package com.iliasen.delivcost.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private Route route;

    @Column(columnDefinition = "VARCHAR(50) DEFAULT 'WITHOUT'")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Cargo cargo;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;


    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
