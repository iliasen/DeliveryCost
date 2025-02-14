package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.dto.NotificationDTO;
import com.iliasen.delivcost.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/notify")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(notificationService.getAllNotifications(userDetails));
    }

    @PutMapping(value = "/subscribe/{id}")
    public ResponseEntity<String> setSubscribe(@PathVariable Long id,@RequestParam boolean subscribe, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(notificationService.changeSubscribe(id, subscribe, userDetails));
    }

    @PutMapping(value = "/view/{id}")
    public ResponseEntity<String> viewingTheNotify(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(notificationService.viewNotify(id, userDetails));
    }

    @DeleteMapping
    public ResponseEntity<String> delNotifications(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(notificationService.deleteAllNotifications(userDetails));
    }
}
