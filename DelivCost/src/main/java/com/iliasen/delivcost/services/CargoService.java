package com.iliasen.delivcost.services;

import com.iliasen.delivcost.models.Cargo;
import com.iliasen.delivcost.models.Order;
import com.iliasen.delivcost.models.Warehouse;
import com.iliasen.delivcost.repositories.CargoRepository;
import com.iliasen.delivcost.repositories.ClientRepository;
import com.iliasen.delivcost.repositories.PartnerRepository;
import com.iliasen.delivcost.repositories.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;
    private final WarehouseRepository warehouseRepository;

    public void addCargo(Cargo cargo, Order order) {
        cargo.setOrder(order);
        cargoRepository.save(cargo);
    }

    public ResponseEntity<?> getCargo(Integer id){
        Cargo cargo = cargoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return ResponseEntity.ok(cargo);
    }


    public void transferCargoToWarehouse(Order order) {
        Cargo cargo = cargoRepository.findByOrderId(order.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        Warehouse warehouse = warehouseRepository.findByClientId(order.getClient().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));;
        List<Cargo> cargos = warehouse.getCargos();
        cargos.add(cargo);
        warehouse.setCargos(cargos);
        //добавить расчет влизает ли заказ
        warehouseRepository.save(warehouse);
    }
}
