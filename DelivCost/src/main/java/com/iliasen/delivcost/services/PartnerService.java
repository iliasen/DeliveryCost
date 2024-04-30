package com.iliasen.delivcost.services;

import com.iliasen.delivcost.models.Partner;
import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.repositories.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;


    public ResponseEntity<List<Partner>> getAll() {
        Iterable<Partner> partnersIterable = partnerRepository.findAll();
        List<Partner> partners = new ArrayList<>();
        partnersIterable.forEach(partners::add);
        return ResponseEntity.ok(partners);
    }

    public ResponseEntity<Partner> getOne(Integer id) {
        Partner partner = partnerRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(partner);
    }

    public <T> ResponseEntity<String> updateField(String field, T value, String successMessage, UserDetails userDetails) {
                Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

        switch (field) {
            case "companyOfficial":
                partner.setCompanyOfficial(value.toString());
                break;
            case "description":
                partner.setDescription(value.toString());
                break;
            case "margin":
                    partner.setMargin((int) value);
                break;
            case "transport":
                if (value instanceof Set) {
                    Set<Transport> transportList = (Set<Transport>) value;
                    partner.setTransportList(transportList);
                } else {
                    return ResponseEntity.badRequest().body("Invalid value for the transport field.");
                }
                break;
            default:
                return ResponseEntity.badRequest().body("Invalid field: " + field);
        }

        partnerRepository.save(partner);
        return ResponseEntity.ok(successMessage);

    }

    public ResponseEntity<String> setCompanyLogo(MultipartFile img, UserDetails userDetails) throws IOException {

        Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

        if (img != null && !img.isEmpty()) {
            String fileName = UUID.randomUUID() + ".jpg";
            String uploadDir = "src/main/resources/static";

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            if (partner.getCompanyLogo() != null) {
                Path oldFilePath = uploadPath.resolve(partner.getCompanyLogo());
                Files.deleteIfExists(oldFilePath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(img.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            partner.setCompanyLogo(fileName);
            partnerRepository.save(partner);
        }
        return ResponseEntity.ok("Company logo successfully update");
    }

    public boolean check(UserDetails userDetails) {
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

        boolean isLogoMissing = partner.getCompanyLogo() == null;
        boolean isMarginZero = partner.getMargin() == 0;
        boolean isOfficialMissing = partner.getCompanyOfficial() == null;
        boolean isDescriptionMissing = partner.getDescription() == null;

        return !(isLogoMissing || isMarginZero || isOfficialMissing || isDescriptionMissing);
    }

    public ResponseEntity<Resource> getImg(String imageName) throws MalformedURLException {
        String uploadDir = "src/main/resources/static/";
        Path imagePath = Paths.get(uploadDir + imageName);
        Resource resource = new UrlResource(imagePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
