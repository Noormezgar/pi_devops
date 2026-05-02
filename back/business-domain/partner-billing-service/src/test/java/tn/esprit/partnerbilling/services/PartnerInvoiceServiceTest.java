package tn.esprit.partnerbilling.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.partnerbilling.entities.PartnerInvoice;
import tn.esprit.partnerbilling.repositories.PartnerInvoiceRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartnerInvoiceServiceTest {

    @Mock
    private PartnerInvoiceRepository invoiceRepository;

    @InjectMocks
    private PartnerInvoiceService invoiceService;

    private PartnerInvoice invoice;

    @BeforeEach
    void setUp() {
        invoice = new PartnerInvoice();
        invoice.setId(1L);
        invoice.setPartnerId(7L);
        invoice.setInvoiceNumber("INV-001");
        invoice.setStatus("PENDING");
    }

    @Test
    void getAllInvoicesReturnsRepositoryData() {
        when(invoiceRepository.findAll()).thenReturn(List.of(invoice));

        List<PartnerInvoice> result = invoiceService.getAllInvoices();

        assertEquals(1, result.size());
        assertEquals("INV-001", result.get(0).getInvoiceNumber());
        verify(invoiceRepository).findAll();
    }

    @Test
    void getInvoicesByPartnerFiltersByPartnerId() {
        when(invoiceRepository.findByPartnerId(7L)).thenReturn(List.of(invoice));

        List<PartnerInvoice> result = invoiceService.getInvoicesByPartner(7L);

        assertEquals(1, result.size());
        assertEquals(7L, result.get(0).getPartnerId());
        verify(invoiceRepository).findByPartnerId(7L);
    }

    @Test
    void generateInvoiceSavesInvoice() {
        when(invoiceRepository.save(invoice)).thenReturn(invoice);

        PartnerInvoice result = invoiceService.generateInvoice(invoice);

        assertEquals("INV-001", result.getInvoiceNumber());
        verify(invoiceRepository).save(invoice);
    }

    @Test
    void getInvoiceByIdThrowsWhenMissing() {
        when(invoiceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> invoiceService.getInvoiceById(99L));
    }

    @Test
    void markAsPaidUpdatesStatusAndPaidDate() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(any(PartnerInvoice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PartnerInvoice result = invoiceService.markAsPaid(1L);

        assertEquals("PAID", result.getStatus());
        assertNotNull(result.getPaidAt());
        verify(invoiceRepository).save(invoice);
    }

    @Test
    void updateStatusChangesInvoiceStatus() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(any(PartnerInvoice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PartnerInvoice result = invoiceService.updateStatus(1L, "OVERDUE");

        assertEquals("OVERDUE", result.getStatus());
        verify(invoiceRepository).save(invoice);
    }
}
