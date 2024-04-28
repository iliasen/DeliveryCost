package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.dto.PartnerFieldDTO;
import com.iliasen.delivcost.models.Partner;
import com.iliasen.delivcost.services.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/partner")
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping(value = "/all")
    public ResponseEntity<List<Partner>> getPartners(){
        return partnerService.getAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Partner> getPartner(@PathVariable Integer id){
        return partnerService.getOne(id);
    }

    @GetMapping(value = "/fields_check")
    public boolean checkAllFields(@AuthenticationPrincipal UserDetails userDetails){
        return partnerService.check(userDetails);
    }

    @PutMapping(value = "/update_fields")
    public ResponseEntity<?> updateFields(@RequestParam("companyOfficial") String companyOfficial,
                                          @RequestParam("description") String description,
                                          @RequestParam("margin") int margin,
                                          @RequestParam("img") MultipartFile image, @AuthenticationPrincipal UserDetails userDetails){
        partnerService.updateField("companyOfficial", companyOfficial, "Официальный представитель успешно добавлен",userDetails);
        partnerService.updateField("description", description, "Описание компании добавлено.", userDetails);
        partnerService.updateField("margin", margin, "Добавлена маржа компании", userDetails);
        try {
            partnerService.setCompanyLogo(image,userDetails);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка при загрузке логотипа компании");
        }
        return ResponseEntity.ok("Все поля успешно обновлены");
    }

    @PatchMapping(value = "/official")
    public ResponseEntity<String> updateCompanyOfficial(@RequestBody String official,@AuthenticationPrincipal UserDetails userDetails){
        return partnerService.updateField("companyOfficial", official, "Официальный представитель успешно добавлен: " + official,userDetails);
    }

    @PatchMapping(value = "/description")
    public ResponseEntity<String> updateDescription(@RequestBody String description,@AuthenticationPrincipal UserDetails userDetails) {
        return partnerService.updateField("description", description, "Описание компании добавлено.", userDetails);
    }

    @PatchMapping(value = "/margin")
    public ResponseEntity<String> updateMargin(@RequestBody float margin,@AuthenticationPrincipal UserDetails userDetails) {
        return partnerService.updateField("margin", margin, "Добавлена маржа компании:" + margin, userDetails);
    }

    @PatchMapping(value = "/transport")
    public ResponseEntity<String> updateTransport(@RequestBody float margin,@AuthenticationPrincipal UserDetails userDetails) {
        return partnerService.updateField("transport", margin, "Добавлена транспорт компании", userDetails);
    }

    @PutMapping(value = "/img")
    public ResponseEntity<String> setLogo(@RequestParam("img") MultipartFile image,@AuthenticationPrincipal UserDetails userDetails){
        try {
            return partnerService.setCompanyLogo(image,userDetails);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка при загрузке логотипа компании");
        }
    }
}
