package com.iliasen.delivcost.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliasen.delivcost.dto.NotificationDTO;
import com.iliasen.delivcost.models.OrderStatus;
import com.iliasen.delivcost.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void getNotificationsTest() throws Exception {

        NotificationDTO notification1 = new NotificationDTO(
                1L,
                OrderStatus.NEW,
                true,
                false,
                LocalDateTime.of(2023, 10, 1, 12, 0),
                null,
                null,
                null
        );

        NotificationDTO notification2 = new NotificationDTO(
                2L,
                OrderStatus.DELIVERED,
                false,
                true,
                LocalDateTime.of(2023, 10, 2, 12, 0),
                null,
                null,
                null
        );

        Page<NotificationDTO> notificationsPage = new PageImpl<>(List.of(notification1, notification2));
        when(notificationService.getAllNotifications(any(Pageable.class), any()))
                .thenReturn(notificationsPage);

        MvcResult result = mockMvc.perform(get("/notify")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].newStatus").value("NEW"))
                .andReturn();

        System.out.println("getNotificationsTest response: " + result.getResponse().getContentAsString());
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void setSubscribeTest() throws Exception {
        Long notificationId = 1L;
        boolean subscribe = true;
        String expectedResponse = "Subscription updated successfully";

        when(notificationService.changeSubscribe(eq(notificationId), eq(subscribe), any()))
                .thenReturn(expectedResponse);

        MvcResult result = mockMvc.perform(put("/notify/subscribe/{id}", notificationId)
                        .param("subscribe", String.valueOf(subscribe)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse))
                .andReturn();

        System.out.println("setSubscribeTest response: " + result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void viewingTheNotifyTest() throws Exception {
        Long notificationId = 1L;
        String expectedResponse = "Notification viewed";

        when(notificationService.viewNotify(eq(notificationId), any()))
                .thenReturn(expectedResponse);

        MvcResult result = mockMvc.perform(put("/notify/view/{id}", notificationId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse))
                .andReturn();

        System.out.println("viewingTheNotifyTest response: " + result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void delNotificationsTest() throws Exception {
        String expectedResponse = "All notifications deleted";

        when(notificationService.deleteAllNotifications(any()))
                .thenReturn(expectedResponse);

        MvcResult result = mockMvc.perform(delete("/notify"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse))
                .andReturn();

        System.out.println("delNotificationsTest response: " + result.getResponse().getContentAsString());
    }
}