package com.iliasen.delivcost.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="cargos")
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private int weight;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

}
