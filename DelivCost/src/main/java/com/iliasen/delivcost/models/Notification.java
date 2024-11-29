package com.iliasen.delivcost.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus newStatus;

    @Column
    private boolean partnerChecked = false;

    @Column
    private boolean clientChecked = false;

    @Column
    private LocalDateTime changeTime;

    @ManyToOne
    private Order order;

    @JsonIgnore
    @ManyToOne
    private Partner partner;

    @JsonIgnore
    @ManyToOne
    private Client client;
}