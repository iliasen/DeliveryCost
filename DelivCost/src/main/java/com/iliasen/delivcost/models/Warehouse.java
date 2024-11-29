package com.iliasen.delivcost.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Warehouses")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private int length = 1600;

    @Column
    private int width = 1500;

    @Column
    private int height = 450;

    @OneToMany(mappedBy = "warehouse",cascade = CascadeType.ALL)
    private List<Cargo> cargos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    public int getVolume(){
        return length * width * height;
    }
}
