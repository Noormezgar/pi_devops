package com.example.controller;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.entity.Partner;
import com.example.service.PartnerService;

@WebMvcTest(PartnerController.class)
public class PartnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartnerService partnerService;

    @Test
    public void testGetAllPartners() throws Exception {
        Partner partner1 = new Partner("Partner1", "email1@test.com", "123456");
        Partner partner2 = new Partner("Partner2", "email2@test.com", "789012");
        partner1.setId(1L);
        partner2.setId(2L);

        when(partnerService.getAllPartners()).thenReturn(Arrays.asList(partner1, partner2));

        mockMvc.perform(get("/api/partners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Partner1"))
                .andExpect(jsonPath("$[1].name").value("Partner2"));
    }

    @Test
    public void testGetPartnerById_Found() throws Exception {
        Partner partner = new Partner("Partner1", "email1@test.com", "123456");
        partner.setId(1L);

        when(partnerService.getPartnerById(1L)).thenReturn(Optional.of(partner));

        mockMvc.perform(get("/api/partners/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Partner1"))
                .andExpect(jsonPath("$.contactEmail").value("email1@test.com"));
    }

    @Test
    public void testGetPartnerById_NotFound() throws Exception {
        when(partnerService.getPartnerById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/partners/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreatePartner() throws Exception {
        Partner partner = new Partner("NewPartner", "new@test.com", "5551234");
        partner.setId(1L);

        when(partnerService.createPartner(any(Partner.class))).thenReturn(partner);

        mockMvc.perform(post("/api/partners")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"NewPartner\",\"contactEmail\":\"new@test.com\",\"contactPhone\":\"5551234\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("NewPartner"))
                .andExpect(jsonPath("$.contactEmail").value("new@test.com"));
    }

    @Test
    public void testUpdatePartner() throws Exception {
        Partner partner = new Partner("UpdatedPartner", "updated@test.com", "5555678");
        partner.setId(1L);

        when(partnerService.updatePartner(anyLong(), any(Partner.class))).thenReturn(partner);

        mockMvc.perform(put("/api/partners/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"UpdatedPartner\",\"contactEmail\":\"updated@test.com\",\"contactPhone\":\"5555678\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedPartner"));
    }

    @Test
    public void testDeletePartner() throws Exception {
        mockMvc.perform(delete("/api/partners/1"))
                .andExpect(status().isNoContent());
    }
}