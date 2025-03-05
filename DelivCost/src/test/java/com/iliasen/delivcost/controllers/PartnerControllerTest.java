package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.dto.PartnerDTO;
import com.iliasen.delivcost.models.Role;
import com.iliasen.delivcost.services.PartnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PartnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartnerService partnerService;

    @Test
    @WithMockUser
    public void testGetPartners() throws Exception {

        PartnerDTO partnerDto = new PartnerDTO(
                42L,
                "CompanyName",
                123456789L,
                "email@example.com",
                "123-456-7890",
                "John Doe",
                "Sample description",
                10,
                "logo.png",
                Role.PARTNER
        );

        Page<PartnerDTO> page = new PageImpl<>(List.of(partnerDto));

        when(partnerService.getAll(any(Pageable.class), any(), any())).thenReturn(page);

        mockMvc.perform(get("/partner/all")
                        .param("sortByCompanyNameAsc", "true")
                        .param("sortByRatingAsc", "false"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetPartnerById() throws Exception {
        PartnerDTO partnerDto = new PartnerDTO(
                42L,
                "CompanyName",
                123456789L,
                "email@example.com",
                "123-456-7890",
                "John Doe",
                "Sample description",
                10,
                "logo.png",
                Role.PARTNER
        );
        when(partnerService.getOne(42L)).thenReturn(partnerDto);

        mockMvc.perform(get("/partner/42"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "partner@example.com")
    public void testCheckAllFields() throws Exception {
        when(partnerService.check(any())).thenReturn(true);

        mockMvc.perform(get("/partner/fields_check"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockUser
    public void testGetLogoImage() throws Exception {
        byte[] data = "dummy image data".getBytes();
        Resource resource = new ByteArrayResource(data);
        when(partnerService.getImg("logo.png")).thenReturn(ResponseEntity.ok(resource));

        mockMvc.perform(get("/partner/img/logo.png"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "partner@example.com")
    public void testUpdateFields() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("img", "logo.png",
                MediaType.IMAGE_PNG_VALUE, "dummy".getBytes());
        when(partnerService.setCompanyLogo(any(), any()))
                .thenReturn(ResponseEntity.ok("Logo updated"));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/partner/update_fields")
                        .file(imageFile)
                        .param("companyOfficial", "Official Test")
                        .param("description", "Test description")
                        .param("margin", "10")
                        // Нам нужно установить метод на PUT (по умолчанию multipart-запросы идут как POST)
                        .with(request -> { request.setMethod("PUT"); return request; })
                        .principal(() -> "partner@example.com")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("All fields successfully updated"));
    }

    @Test
    @WithMockUser(username = "partner@example.com")
    public void testUpdateCompanyOfficial() throws Exception {
        String official = "New Official";
        String responseMessage = "Official representative successfully added: " + official;
        when(partnerService.updateField(eq("companyOfficial"), eq(official), eq(responseMessage), any()))
                .thenReturn(ResponseEntity.ok(responseMessage));

        mockMvc.perform(patch("/partner/official")
                        .content(official)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage));
    }

    @Test
    @WithMockUser(username = "partner@example.com")
    public void testUpdateDescription() throws Exception {
        String description = "New Description";
        String responseMessage = "Company description added.";
        when(partnerService.updateField(eq("description"), eq(description), eq(responseMessage), any()))
                .thenReturn(ResponseEntity.ok(responseMessage));

        mockMvc.perform(patch("/partner/description")
                        .content(description)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage));
    }

    @Test
    @WithMockUser(username = "partner@example.com")
    public void testUpdateMargin() throws Exception {
        float margin = 12.5f;
        String responseMessage = "Company margin added: " + margin;
        when(partnerService.updateField(eq("margin"), eq(margin), eq(responseMessage), any()))
                .thenReturn(ResponseEntity.ok(responseMessage));

        mockMvc.perform(patch("/partner/margin")
                        .content(String.valueOf(margin))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage));
    }

    @Test
    @WithMockUser(username = "partner@example.com")
    public void testUpdateTransport() throws Exception {
        float transport = 8.0f;
        String responseMessage = "Company transport added";
        when(partnerService.updateField(eq("transport"), eq(transport), eq(responseMessage), any()))
                .thenReturn(ResponseEntity.ok(responseMessage));

        mockMvc.perform(patch("/partner/transport")
                        .content(String.valueOf(transport))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage));
    }

    @Test
    @WithMockUser(username = "partner@example.com")
    public void testSetLogo() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("img", "logo.png",
                MediaType.IMAGE_PNG_VALUE, "dummy".getBytes());
        String responseMessage = "Logo set";
        when(partnerService.setCompanyLogo(any(), any()))
                .thenReturn(ResponseEntity.ok(responseMessage));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/partner/img")
                        .file(imageFile)
                        .with(request -> { request.setMethod("PUT"); return request; })
                        .principal(() -> "partner@example.com")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage));
    }
}
