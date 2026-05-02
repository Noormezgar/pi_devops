package com.example.controller;

import java.time.LocalDate;
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

import com.example.entity.Deal;
import com.example.service.DealService;

@WebMvcTest(DealController.class)
public class DealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DealService dealService;

    @Test
    public void testGetAllDeals() throws Exception {
        Deal deal1 = new Deal("Deal1", "Description1", 1L, LocalDate.now(), LocalDate.now().plusDays(30));
        Deal deal2 = new Deal("Deal2", "Description2", 2L, LocalDate.now(), LocalDate.now().plusDays(60));
        deal1.setId(1L);
        deal2.setId(2L);

        when(dealService.getAllDeals()).thenReturn(Arrays.asList(deal1, deal2));

        mockMvc.perform(get("/api/deals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Deal1"))
                .andExpect(jsonPath("$[1].title").value("Deal2"));
    }

    @Test
    public void testGetDealById_Found() throws Exception {
        Deal deal = new Deal("Deal1", "Description1", 1L, LocalDate.now(), LocalDate.now().plusDays(30));
        deal.setId(1L);

        when(dealService.getDealById(1L)).thenReturn(Optional.of(deal));

        mockMvc.perform(get("/api/deals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Deal1"))
                .andExpect(jsonPath("$.description").value("Description1"));
    }

    @Test
    public void testGetDealById_NotFound() throws Exception {
        when(dealService.getDealById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/deals/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateDeal() throws Exception {
        Deal deal = new Deal("New Deal", "New Description", 1L, LocalDate.now(), LocalDate.now().plusDays(30));
        deal.setId(1L);

        when(dealService.createDeal(any(Deal.class))).thenReturn(deal);

        mockMvc.perform(post("/api/deals")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"New Deal\",\"description\":\"New Description\",\"partnerId\":1,\"startDate\":\"2026-04-30\",\"endDate\":\"2026-05-30\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Deal"));
    }

    @Test
    public void testUpdateDeal() throws Exception {
        Deal deal = new Deal("Updated Deal", "Updated Description", 1L, LocalDate.now(), LocalDate.now().plusDays(30));
        deal.setId(1L);

        when(dealService.updateDeal(anyLong(), any(Deal.class))).thenReturn(deal);

        mockMvc.perform(put("/api/deals/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated Deal\",\"description\":\"Updated Description\",\"partnerId\":1,\"startDate\":\"2026-04-30\",\"endDate\":\"2026-05-30\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Deal"));
    }

    @Test
    public void testDeleteDeal() throws Exception {
        mockMvc.perform(delete("/api/deals/1"))
                .andExpect(status().isNoContent());
    }
}