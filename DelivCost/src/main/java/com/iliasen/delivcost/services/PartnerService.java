package com.iliasen.delivcost.services;

import com.iliasen.delivcost.models.Partner;
import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.repositories.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public <T> ResponseEntity<String> updateField(String field, T value, String successMessage) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                Partner partner = partnerRepository.findByEmail(userDetails.getUsername()).orElse(null);

                switch (field) {
                    case "companyOfficial":
                        partner.setCompanyOfficial(value.toString());
                        break;
                    case "description":
                        partner.setDescription(value.toString());
                        break;
                    case "margin":
                        if (value instanceof Float) {
                            partner.setMargin((Float) value);
                        } else {
                            return ResponseEntity.badRequest().body("Недопустимое значение для поля margin.");
                        }
                        break;
                    case "transport":
                        if (value instanceof Set) {
                            Set<Transport> transportList = (Set<Transport>) value;
                            partner.setTransportList(transportList);
                        } else {
                            return ResponseEntity.badRequest().body("Недопустимое значение для поля transport.");
                        }
                        break;
                    default:
                        return ResponseEntity.badRequest().body("Недопустимое поле: " + field);
                }

                partnerRepository.save(partner);
                return ResponseEntity.ok(successMessage);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Нет доступа !");
    }

    public ResponseEntity<String> setCompanyLogo(MultipartFile img) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                Partner partner = partnerRepository.findByEmail(userDetails.getUsername()).orElse(null);

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
                return ResponseEntity.ok("Логотип компании успешно обновлен");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Нет доступа !");
    }


}
