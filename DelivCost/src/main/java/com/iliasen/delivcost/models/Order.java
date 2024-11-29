package com.iliasen.delivcost.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
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

    @Column
    private boolean partnerChecked = false;

    @Column
    private boolean clientSubscribe = true;

    @Column
    private String comment;

    @Column
    private int price = 400;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.WITHOUT;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Cargo cargo;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<Notification> notification;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

}
