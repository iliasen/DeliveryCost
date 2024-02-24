package com.iliasen.delivcost.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "storages")
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private float volume;

    @OneToMany(mappedBy = "storage",cascade = CascadeType.ALL)
    private List<Cargo> cargos;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
