package com.iliasen.delivcost.services;

import com.iliasen.delivcost.models.Cargo;
import com.iliasen.delivcost.models.Warehouse;
import com.iliasen.delivcost.repositories.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;


    public ResponseEntity<?> getFreeSpace(UserDetails userDetails) {
        Warehouse warehouse = warehouseRepository.findByClientEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Warehouse not found"));

        List<Cargo> cargos = warehouse.getCargos();


        double totalVolume = 0.0;

        for (Cargo cargo : cargos) {
            double volume = cargo.getVolume();
            totalVolume += volume;
        }

        // Вычисление свободного места на складе
        int freeSpace = (int) (warehouse.getVolume() - totalVolume);

        // Формирование ответа
        Map<String, Integer> response = new HashMap<>();
        response.put("totalVolume", (int) totalVolume);
        response.put("freeSpace", freeSpace);

        return ResponseEntity.ok(response);
    }
}
