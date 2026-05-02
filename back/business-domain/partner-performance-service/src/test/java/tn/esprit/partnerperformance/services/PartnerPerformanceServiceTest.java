package tn.esprit.partnerperformance.services;

import org.junit.jupiter.api.Test;
import tn.esprit.partnerperformance.dto.LeaderboardRow;
import tn.esprit.partnerperformance.dto.PartnerKpi;
import tn.esprit.partnerperformance.entities.PerformanceAlert;
import tn.esprit.partnerperformance.repositories.PerformanceAlertRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PartnerPerformanceServiceTest {

    @Test
    void getPartnerKpiReturnsRealisticValues() {
        PartnerPerformanceService service = new PartnerPerformanceService(emptyRepository());

        PartnerKpi result = service.getPartnerKpi(1L, "30d");

        assertTrue(result.getRedemptionRate() >= 0);
        assertTrue(result.getIssued() > 0);
        assertTrue(result.getRedeemed() > 0);
        assertTrue(result.getRevenue() > 0);
    }

    @Test
    void getLeaderboardHonorsLimit() {
        PartnerPerformanceService service = new PartnerPerformanceService(emptyRepository());

        List<LeaderboardRow> result = service.getLeaderboard("30d", "revenue", 3);

        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getRank());
    }

    @Test
    void getOpenAlertsDelegatesToRepository() {
        PerformanceAlertRepository repository = emptyRepository();
        PerformanceAlert alert = alert();
        when(repository.findByStatusOrderByCreatedAtDesc("OPEN")).thenReturn(List.of(alert));
        PartnerPerformanceService service = new PartnerPerformanceService(repository);

        List<PerformanceAlert> result = service.getOpenAlerts();

        assertEquals(1, result.size());
        verify(repository).findByStatusOrderByCreatedAtDesc("OPEN");
    }

    @Test
    void resolveAlertMarksAlertAsResolved() {
        PerformanceAlertRepository repository = emptyRepository();
        PerformanceAlert alert = alert();
        when(repository.findById(1L)).thenReturn(Optional.of(alert));
        when(repository.save(alert)).thenReturn(alert);
        PartnerPerformanceService service = new PartnerPerformanceService(repository);

        PerformanceAlert result = service.resolveAlert(1L);

        assertEquals("RESOLVED", result.getStatus());
        assertNotNull(result.getResolvedAt());
    }

    private PerformanceAlertRepository emptyRepository() {
        PerformanceAlertRepository repository = mock(PerformanceAlertRepository.class);
        when(repository.count()).thenReturn(1L);
        return repository;
    }

    private PerformanceAlert alert() {
        return new PerformanceAlert(1L, 2L, "LOW_REDEMPTION", "HIGH", "Low redemption", "OPEN", LocalDateTime.now(), null);
    }
}
