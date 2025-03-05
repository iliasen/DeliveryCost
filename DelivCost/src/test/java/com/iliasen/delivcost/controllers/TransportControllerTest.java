package com.iliasen.delivcost.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliasen.delivcost.dto.TransportDTO;
import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.models.TransportType;
import com.iliasen.delivcost.services.TransportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class TransportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransportService transportService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "PARTNER")
    void create() throws Exception {
        Transport transport = new Transport();
        transport.setTransportType(TransportType.SHIP);
        transport.setTonnage(5000);
        transport.setVolume(3000);

        String transportJson = objectMapper.writeValueAsString(transport);

        mockMvc.perform(post("/transport")
                        .content(transportJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "PARTNER")
    void testGetTransport() throws Exception {
        TransportDTO transportDto = new TransportDTO();
        transportDto.setId(1L);
        transportDto.setTransportType(TransportType.SHIP);
        transportDto.setTonnage(5000);
        transportDto.setVolume(3000);

        Page<TransportDTO> transportPage = new PageImpl<>(List.of(transportDto));

        when(transportService.getTransport(
                any(Pageable.class),
                any(),
                eq(123L),
                eq("SHIP"),
                eq(true),
                eq(false)
        )).thenReturn(transportPage);

        mockMvc.perform(get("/transport")
                        .param("transportType", "SHIP")
                        .param("partnerId", "123")
                        .param("onlyWithDriver", "true")
                        .param("uniqueTypes", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].transportType").value("SHIP"));
    }
}