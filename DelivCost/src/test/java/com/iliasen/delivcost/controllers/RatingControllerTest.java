package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.dto.RatingDTO;
import com.iliasen.delivcost.dto.mapper.RatingMapper;
import com.iliasen.delivcost.models.Rating;
import com.iliasen.delivcost.services.RatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RatingMapper ratingMapper;

    @Test
    @WithMockUser
    public void testAddRating() throws Exception {
        Long entityId = 1L;

        Rating reqRating = new Rating();
        reqRating.setRate(5);
        reqRating.setFeedback("Excellent!");

        Rating responseRating = new Rating();
        responseRating.setId(100L);
        reqRating.setRate(5);
        reqRating.setFeedback("Excellent!");
        RatingDTO ratingDTO = ratingMapper.toRatingDTO(responseRating);
        when(ratingService.createRating(eq(entityId), any(Rating.class), any()))
                .thenReturn(ratingDTO);

        // Преобразуем объекты в JSON
        String jsonRequest = objectMapper.writeValueAsString(reqRating);
        String jsonResponse = objectMapper.writeValueAsString(responseRating);

        mockMvc.perform(post("/rating/{id}", entityId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    public void testGetRating() throws Exception {
        Long ratingId = 100L;

        Rating rating = new Rating();
        rating.setId(ratingId);
        rating.setRate(4);
        rating.setFeedback("Good!");

        RatingDTO ratingDTO = ratingMapper.toRatingDTO(rating);

        when(ratingService.getById(ratingId)).thenReturn(Collections.singletonList(ratingDTO));

        mockMvc.perform(get("/rating/{id}", ratingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAverageRating() throws Exception {
        Long entityId = 1L;
        Double average = 4.2;

        when(ratingService.getAverageRating(entityId)).thenReturn(average);

        mockMvc.perform(get("/rating/average/{id}", entityId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(average));
    }

    @Test
    @WithMockUser
    public void testDeleteRating() throws Exception {
        Long ratingId = 100L;
        String deleteMessage = "Rating deleted successfully";

        when(ratingService.deleteRatingById(eq(ratingId), any()))
                .thenReturn(deleteMessage);

        mockMvc.perform(delete("/rating/{id}", ratingId))
                .andExpect(status().isOk())
                .andExpect(content().string(deleteMessage));
    }
}