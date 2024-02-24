package com.iliasen.delivcost.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "tansports")
public class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @Enumerated(EnumType.STRING)
    private TransportType transportType;

    @Column
    private float tonnage;

    @Column
    private float volume;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;
}