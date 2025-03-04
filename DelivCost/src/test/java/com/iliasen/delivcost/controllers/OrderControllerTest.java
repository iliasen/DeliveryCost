package com.iliasen.delivcost.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliasen.delivcost.dto.CargoDTO;
import com.iliasen.delivcost.dto.OrderAndCargoRequest;
import com.iliasen.delivcost.dto.OrderDTO;
import com.iliasen.delivcost.dto.OrderListDTO;
import com.iliasen.delivcost.models.OrderStatus;
import com.iliasen.delivcost.models.Route;
import com.iliasen.delivcost.services.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testCreateOrder() throws Exception {
        CargoDTO cargoDTO = new CargoDTO();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setOrderStatus("Created");

        OrderAndCargoRequest request = new OrderAndCargoRequest(orderDTO, cargoDTO);
        UserDetails userDetails = Mockito.mock(UserDetails.class);


        when(orderService.addOrder(eq(request), anyLong(), eq(userDetails))).thenReturn(orderDTO);

        mockMvc.perform(post("/order/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderDTO.getId()))
                .andExpect(jsonPath("$.orderStatus").value(orderDTO.getOrderStatus()));
    }

    @Test
    @WithMockUser
    public void testGetOrders() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setOrderStatus("Created");
        Page<OrderDTO> orders = new PageImpl<>(Collections.singletonList(orderDTO));

        when(orderService.getOrders(eq(pageable), eq("status"), any(UserDetails.class), eq(1L), eq(2L), eq(3L), eq(true))).thenReturn(orders);

        mockMvc.perform(get("/order")
                        .param("status", "status")
                        .param("driverId", "1")
                        .param("partnerId", "2")
                        .param("clientId", "3")
                        .param("forTransfer", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(orders.getContent().get(0).getId()))
                .andExpect(jsonPath("$.content[0].orderStatus").value(orders.getContent().get(0).getOrderStatus()));
    }

    @Test
    @WithMockUser
    public void testTransferOrdersToTheDriver() throws Exception {
        OrderListDTO orderListDTO = new OrderListDTO(Arrays.asList(new OrderDTO(), new OrderDTO()));
        when(orderService.transferOrdersToTheDriver(anyLong(), anyList())).thenReturn("Success");

        mockMvc.perform(post("/order/transfer/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderListDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }

    @Test
    @WithMockUser
    public void testOrder() throws Exception {
        OrderListDTO orderListDTO = new OrderListDTO(Arrays.asList(new OrderDTO(), new OrderDTO()));

        mockMvc.perform(post("/order/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderListDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    @WithMockUser(authorities = "PARTNER")
    public void testGetNewOrders() throws Exception {
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        List<OrderDTO> newOrders = Arrays.asList(new OrderDTO(), new OrderDTO());

        when(orderService.getNewOrders(eq(userDetails))).thenReturn(newOrders);

        mockMvc.perform(get("/order/new")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());
    }
}
