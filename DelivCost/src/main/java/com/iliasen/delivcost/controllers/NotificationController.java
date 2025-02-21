package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.dto.NotificationDTO;
import com.iliasen.delivcost.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/notify")
@Tag(name = "Notifications", description = "Interactions with notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "Get all notifications", description = "Returns a list of all notifications for the user")
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(notificationService.getAllNotifications(userDetails));
    }

    @Operation(summary = "Subscribe or unsubscribe to notifications", description = "Changes the subscription status for a specific notification")
    @PutMapping(value = "/subscribe/{id}")
    public ResponseEntity<String> setSubscribe(
            @PathVariable Long id,
            @RequestParam boolean subscribe,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(notificationService.changeSubscribe(id, subscribe, userDetails));
    }

    @Operation(summary = "Mark notification as viewed", description = "Marks a specific notification as viewed")
    @PutMapping(value = "/view/{id}")
    public ResponseEntity<String> viewingTheNotify(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(notificationService.viewNotify(id, userDetails));
    }

    @Operation(summary = "Delete all notifications", description = "Deletes all notifications for the user")
    @DeleteMapping
    public ResponseEntity<String> delNotifications(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(notificationService.deleteAllNotifications(userDetails));
    }
}
