package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.models.Partner;
import com.iliasen.delivcost.services.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/partner")
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping
    public ResponseEntity<List<Partner>> getPartners(){
        return partnerService.getAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Partner> getPartner(@PathVariable Integer id){
        return partnerService.getOne(id);
    }

    @PatchMapping(value = "/official")
    public ResponseEntity<String> updateCompanyOfficial(@RequestBody String official){
        return partnerService.updateField("companyOfficial", official, "Официальный представитель успешно добавлен: " + official);
    }

    @PatchMapping(value = "/description")
    public ResponseEntity<String> updateDescription(@RequestBody String description) {
        return partnerService.updateField("description", description, "Описание компании добавлено.");
    }

    @PatchMapping(value = "/margin")
    public ResponseEntity<String> updateMargin(@RequestBody float margin) {
        return partnerService.updateField("margin", margin, "Добавлена маржа компании:" + margin);
    }

    @PatchMapping(value = "/transport")
    public ResponseEntity<String> updateTransport(@RequestBody float margin) {
        return partnerService.updateField("transport", margin, "Добавлена транспорт компании");
    }

    @PutMapping(value = "/img")
    public ResponseEntity<String> setLogo(@RequestParam("img") MultipartFile image){
        try {
            return partnerService.setCompanyLogo(image);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка при загрузке логотипа компании");
        }
    }
}
