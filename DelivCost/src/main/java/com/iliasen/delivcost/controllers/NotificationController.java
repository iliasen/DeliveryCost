package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/notify")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getNotifications(@AuthenticationPrincipal UserDetails userDetails){
        return notificationService.getAllNotifications(userDetails);
    }

    @PutMapping(value = "/subscribe/{id}")
    public ResponseEntity<?> setSubscribe(@PathVariable Integer id,@RequestParam boolean subscribe, @AuthenticationPrincipal UserDetails userDetails){
        return notificationService.changeSubscribe(id, subscribe, userDetails);
    }

    @PutMapping(value = "/view/{id}")
    public ResponseEntity<?> viewingTheNotify(@PathVariable Integer id, @AuthenticationPrincipal UserDetails userDetails){
        return notificationService.viewNotify(id, userDetails);
    }

    @DeleteMapping
    public ResponseEntity<?> delNotifications(@AuthenticationPrincipal UserDetails userDetails){
        return notificationService.deleteAllNotifications(userDetails);
    }
}
