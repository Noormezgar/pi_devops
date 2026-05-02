package tn.esprit.voucherfraud.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.voucherfraud.entities.FraudAlert;
import tn.esprit.voucherfraud.repositories.FraudAlertRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FraudDetectionServiceTest {

    @Mock
    private FraudAlertRepository fraudAlertRepository;

    @InjectMocks
    private FraudDetectionService fraudDetectionService;

    private FraudAlert alert;

    @BeforeEach
    void setUp() {
        alert = new FraudAlert();
        alert.setId(1L);
        alert.setPartnerId(4L);
        alert.setVoucherCode("CODE-001");
        alert.setStatus("INVESTIGATION_PENDING");
    }

    @Test
    void getAllAlertsReturnsRepositoryData() {
        when(fraudAlertRepository.findAll()).thenReturn(List.of(alert));

        List<FraudAlert> result = fraudDetectionService.getAllAlerts();

        assertEquals(1, result.size());
        assertEquals("CODE-001", result.get(0).getVoucherCode());
    }

    @Test
    void triggerAlertSavesAlert() {
        when(fraudAlertRepository.save(alert)).thenReturn(alert);

        FraudAlert result = fraudDetectionService.triggerAlert(alert);

        assertEquals("CODE-001", result.getVoucherCode());
        verify(fraudAlertRepository).save(alert);
    }

    @Test
    void updateAlertStatusChangesStatus() {
        when(fraudAlertRepository.findById(1L)).thenReturn(Optional.of(alert));
        when(fraudAlertRepository.save(alert)).thenReturn(alert);

        FraudAlert result = fraudDetectionService.updateAlertStatus(1L, "CONFIRMED");

        assertEquals("CONFIRMED", result.getStatus());
    }

    @Test
    void updateAlertStatusThrowsWhenMissing() {
        when(fraudAlertRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> fraudDetectionService.updateAlertStatus(99L, "CONFIRMED"));
    }

    @Test
    void getAlertsByPartnerFiltersByPartnerId() {
        when(fraudAlertRepository.findByPartnerId(4L)).thenReturn(List.of(alert));

        List<FraudAlert> result = fraudDetectionService.getAlertsByPartner(4L);

        assertEquals(4L, result.get(0).getPartnerId());
    }
}
