package tn.esprit.partnerperformance.controllers;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import tn.esprit.partnerperformance.dto.LeaderboardRow;
import tn.esprit.partnerperformance.dto.PartnerKpi;
import tn.esprit.partnerperformance.entities.PerformanceAlert;
import tn.esprit.partnerperformance.services.PartnerPerformanceService;

@WebMvcTest(PartnerPerformanceController.class)
public class PartnerPerformanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartnerPerformanceService performanceService;

    @Test
    public void testGetPartnerKpis() throws Exception {
        PartnerKpi kpi = new PartnerKpi(45.5, 1000L, 455L, 5000.0);

        when(performanceService.getPartnerKpi(1L, "30d")).thenReturn(kpi);

        mockMvc.perform(get("/api/partner-performance/v1/partners/1/kpis")
                .param("period", "30d"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.redemptionRate").value(45.5))
                .andExpect(jsonPath("$.issued").value(1000))
                .andExpect(jsonPath("$.redeemed").value(455))
                .andExpect(jsonPath("$.revenue").value(5000.0));
    }

    @Test
    public void testGetLeaderboard() throws Exception {
        LeaderboardRow row1 = new LeaderboardRow(1, 11L, "Partner A", 100.0);
        LeaderboardRow row2 = new LeaderboardRow(2, 12L, "Partner B", 90.0);

        when(performanceService.getLeaderboard("30d", "redemptionRate", 10))
                .thenReturn(Arrays.asList(row1, row2));

        mockMvc.perform(get("/api/partner-performance/v1/leaderboard")
                .param("period", "30d")
                .param("metric", "redemptionRate")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rank").value(1))
                .andExpect(jsonPath("$[0].partnerName").value("Partner A"))
                .andExpect(jsonPath("$[1].rank").value(2))
                .andExpect(jsonPath("$[1].partnerName").value("Partner B"));
    }

    @Test
    public void testGetAlerts() throws Exception {
        PerformanceAlert alert = new PerformanceAlert(
                1L,
                1L,
                "LOW_REDEMPTION",
                "HIGH",
                "Redemption rate dropped",
                "OPEN",
                LocalDateTime.now(),
                null
        );

        when(performanceService.getOpenAlerts()).thenReturn(Arrays.asList(alert));

        mockMvc.perform(get("/api/partner-performance/v1/alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].type").value("LOW_REDEMPTION"))
                .andExpect(jsonPath("$[0].severity").value("HIGH"))
                .andExpect(jsonPath("$[0].status").value("OPEN"));
    }

    @Test
    public void testResolveAlert() throws Exception {
        PerformanceAlert alert = new PerformanceAlert(
                1L,
                1L,
                "LOW_REDEMPTION",
                "HIGH",
                "Redemption rate dropped",
                "RESOLVED",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(performanceService.resolveAlert(1L)).thenReturn(alert);

        mockMvc.perform(post("/api/partner-performance/v1/alerts/1/resolve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("RESOLVED"));
    }
}