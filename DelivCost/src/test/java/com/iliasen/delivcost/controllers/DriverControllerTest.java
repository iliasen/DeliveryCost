package com.iliasen.delivcost.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliasen.delivcost.dto.DriverDTO;
import com.iliasen.delivcost.dto.OrderDTO;
import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.models.TransportType;
import com.iliasen.delivcost.services.DriverService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DriverService driverService;

    @Test
    @WithMockUser(username = "user", roles = {"PARTNER"})
    public void getAllDriversTest() throws Exception {
        DriverDTO driver1 = new DriverDTO();
        driver1.setId(1L);
        driver1.setFirstName("John");
        driver1.setLastName("Doe");

        DriverDTO driver2 = new DriverDTO();
        driver2.setId(2L);
        driver2.setFirstName("Jane");
        driver2.setLastName("Smith");

        Page<DriverDTO> driversPage = new PageImpl<>(List.of(driver1, driver2));
        when(driverService.getDrivers(any(Pageable.class), any(UserDetails.class), anyBoolean(), anyString(), anyString(), anyString(), anyBoolean()))
                .thenReturn(driversPage);

        MvcResult result = mockMvc.perform(get("/driver/all")
                        .param("email", "anyEmail")
                        .param("phone", "+375 29 123 45 67")
                        .param("freeOnly", "true")
                        .param("sortByFirstNameAsc", "false"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        System.out.println(jsonResponse);

        mockMvc.perform(get("/driver/all")
                        .param("email", "anyEmail")
                        .param("phone", "+375 29 123 45 67")
                        .param("freeOnly", "true")
                        .param("sortByFirstNameAsc", "false"))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "user", roles = {"DRIVER"})
    void getAllOrders() throws Exception {
        Long driverId = 1L;
        List<OrderDTO> orders = Collections.singletonList(new OrderDTO());

        when(driverService.getOrders(driverId)).thenReturn(orders);

        mockMvc.perform(get("/driver/1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser(username = "user", roles = {"PARTNER"})
    void addTransport() throws Exception {
        Transport transport = Transport.builder()
                .transportType(TransportType.CAR)
                .tonnage(5)
                .volume(3000)
                .build();
        Long driverId = 1L;
        when(driverService.addTransportToDriver(driverId, transport)).thenReturn("Transport added successfully");

        MvcResult result = mockMvc.perform(put("/driver/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(transport)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        System.out.println(responseContent);
    }
}