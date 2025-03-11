package com.iliasen.delivcost.services;

import com.iliasen.delivcost.models.Cargo;
import com.iliasen.delivcost.repositories.CargoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CargoServiceTest {

    @InjectMocks
    private CargoService cargoService;

    @Mock
    private CargoRepository cargoRepository;

    @Test
    void addCargo() {
        Cargo expected = Cargo.builder()
                .width(100)
                .weight(400)
                .height(200)
                .length(300)
                .build();


    }
}