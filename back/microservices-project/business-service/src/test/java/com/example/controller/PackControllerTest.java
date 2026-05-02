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

import com.example.entity.Pack;
import com.example.service.PackService;

@WebMvcTest(PackController.class)
public class PackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PackService packService;

    @Test
    public void testGetAllPacks() throws Exception {
        Pack pack1 = new Pack("Basic Pack", "Description1", 12, true);
        Pack pack2 = new Pack("Premium Pack", "Description2", 24, true);
        pack1.setId(1L);
        pack2.setId(2L);

        when(packService.getAllPacks()).thenReturn(Arrays.asList(pack1, pack2));

        mockMvc.perform(get("/api/packs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Basic Pack"))
                .andExpect(jsonPath("$[1].name").value("Premium Pack"));
    }

    @Test
    public void testGetPackById_Found() throws Exception {
        Pack pack = new Pack("Basic Pack", "Description1", 12, true);
        pack.setId(1L);

        when(packService.getPackById(1L)).thenReturn(Optional.of(pack));

        mockMvc.perform(get("/api/packs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Basic Pack"))
                .andExpect(jsonPath("$.description").value("Description1"));
    }

    @Test
    public void testGetPackById_NotFound() throws Exception {
        when(packService.getPackById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/packs/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreatePack() throws Exception {
        Pack pack = new Pack("New Pack", "New Description", 6, true);
        pack.setId(1L);

        when(packService.createPack(any(Pack.class))).thenReturn(pack);

        mockMvc.perform(post("/api/packs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Pack\",\"description\":\"New Description\",\"validityMonths\":6,\"active\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Pack"))
                .andExpect(jsonPath("$.description").value("New Description"));
    }

    @Test
    public void testUpdatePack() throws Exception {
        Pack pack = new Pack("Updated Pack", "Updated Description", 12, false);
        pack.setId(1L);

        when(packService.updatePack(anyLong(), any(Pack.class))).thenReturn(pack);

        mockMvc.perform(put("/api/packs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Pack\",\"description\":\"Updated Description\",\"validityMonths\":12,\"active\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Pack"));
    }

    @Test
    public void testDeletePack() throws Exception {
        mockMvc.perform(delete("/api/packs/1"))
                .andExpect(status().isNoContent());
    }
}