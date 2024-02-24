package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.models.Cargo;
import com.iliasen.delivcost.services.CargoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cargo")
public class CargoController {
    private final CargoService cargoService;

//    @PostMapping(value = "/{id}")
//    public ResponseEntity<?> createCargo(@RequestBody Cargo cargoRequest, @PathVariable Integer id){
//        return cargoService.addCargoToPartner(cargoRequest, id);
//    }
//
//    @GetMapping
//    public ResponseEntity<?> getCargos(){
//        return  cargoService.getAllCargosByUser();
//    }
}
