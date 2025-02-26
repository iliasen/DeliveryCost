package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.dto.PartnerDTO;
import com.iliasen.delivcost.services.PartnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/partner")
@Tag(name = "Partners", description = "Interactions with partners")
public class PartnerController {

    private final PartnerService partnerService;

    @Operation(summary = "Get all partners", description = "Returns a paginated list of all partners")
    @GetMapping(value = "/all")
    public ResponseEntity<Page<PartnerDTO>> getPartners(
            @PageableDefault(page = 0, size = 20) Pageable pageable) {
        return ResponseEntity.ok(partnerService.getAll(pageable));
    }

    @Operation(summary = "Get partner by ID", description = "Returns a specific partner by its ID")
    @GetMapping(value = "/{id}")
    public ResponseEntity<PartnerDTO> getPartner(@PathVariable Long id) {
        return ResponseEntity.ok(partnerService.getOne(id));
    }

    @Operation(summary = "Check all fields", description = "Checks if all fields for the partner are filled")
    @GetMapping(value = "/fields_check")
    public boolean checkAllFields(@AuthenticationPrincipal UserDetails userDetails) {
        return partnerService.check(userDetails);
    }

    @Operation(summary = "Get partner logo image", description = "Returns the logo image of the partner")
    @GetMapping("/img/{imageName:.+}")
    public ResponseEntity<Resource> getLogoImage(@PathVariable String imageName) throws IOException {
        return partnerService.getImg(imageName);
    }

    @Operation(summary = "Update partner fields", description = "Updates multiple fields for the partner")
    @PutMapping(value = "/update_fields")
    public ResponseEntity<?> updateFields(
            @RequestParam("companyOfficial") String companyOfficial,
            @RequestParam("description") String description,
            @RequestParam("margin") int margin,
            @RequestParam("img") MultipartFile image,
            @AuthenticationPrincipal UserDetails userDetails) {
        partnerService.updateField("companyOfficial", companyOfficial, "Official representative successfully added", userDetails);
        partnerService.updateField("description", description, "Company description added.", userDetails);
        partnerService.updateField("margin", margin, "Company margin added", userDetails);
        try {
            partnerService.setCompanyLogo(image, userDetails);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while uploading company logo");
        }
        return ResponseEntity.ok("All fields successfully updated");
    }

    @Operation(summary = "Update company official", description = "Updates the company official field")
    @PatchMapping(value = "/official")
    public ResponseEntity<String> updateCompanyOfficial(@RequestBody String official, @AuthenticationPrincipal UserDetails userDetails) {
        return partnerService.updateField("companyOfficial", official, "Official representative successfully added: " + official, userDetails);
    }

    @Operation(summary = "Update company description", description = "Updates the company description field")
    @PatchMapping(value = "/description")
    public ResponseEntity<String> updateDescription(@RequestBody String description, @AuthenticationPrincipal UserDetails userDetails) {
        return partnerService.updateField("description", description, "Company description added.", userDetails);
    }

    @Operation(summary = "Update company margin", description = "Updates the company margin field")
    @PatchMapping(value = "/margin")
    public ResponseEntity<String> updateMargin(@RequestBody float margin, @AuthenticationPrincipal UserDetails userDetails) {
        return partnerService.updateField("margin", margin, "Company margin added: " + margin, userDetails);
    }

    @Operation(summary = "Update company transport", description = "Updates the company transport field")
    @PatchMapping(value = "/transport")
    public ResponseEntity<String> updateTransport(@RequestBody float margin, @AuthenticationPrincipal UserDetails userDetails) {
        return partnerService.updateField("transport", margin, "Company transport added", userDetails);
    }

    @Operation(summary = "Set company logo", description = "Sets the company logo image")
    @PutMapping(value = "/img")
    public ResponseEntity<String> setLogo(@RequestParam("img") MultipartFile image, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return partnerService.setCompanyLogo(image, userDetails);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while uploading company logo");
        }
    }
}