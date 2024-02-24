package com.iliasen.delivcost.models;

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
    private float weight;

    @Column
    private float length;

    @Column
    private float height;

    @Column
    private float width;

    @OneToOne(mappedBy = "cargo")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;

    public float getVolume(){
        return length * height * width;
    }
}
