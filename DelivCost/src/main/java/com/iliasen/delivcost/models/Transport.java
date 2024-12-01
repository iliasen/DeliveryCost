package com.iliasen.delivcost.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "transports")
public class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @Enumerated(EnumType.STRING)
    private TransportType transportType;

    @Column
    private double tonnage;

    @Column
    private double volume;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
//    @JoinTable(name = "transport_driver",
//            joinColumns = @JoinColumn(name = "transport_id"),
//            inverseJoinColumns = @JoinColumn(name = "driver_id"))
    private Driver driver;
}