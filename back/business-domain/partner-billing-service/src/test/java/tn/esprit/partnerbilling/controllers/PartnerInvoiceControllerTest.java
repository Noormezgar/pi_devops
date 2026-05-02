package tn.esprit.partnerbilling.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.partnerbilling.entities.PartnerInvoice;
import tn.esprit.partnerbilling.services.PartnerInvoiceService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PartnerInvoiceController.class)
class PartnerInvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartnerInvoiceService invoiceService;

    @Test
    void getAllInvoicesReturnsInvoices() throws Exception {
        PartnerInvoice invoice = invoice("INV-001");
        when(invoiceService.getAllInvoices()).thenReturn(List.of(invoice));

        mockMvc.perform(get("/api/partner-billing/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].invoiceNumber").value("INV-001"));
    }

    @Test
    void getInvoicesByPartnerReturnsPartnerInvoices() throws Exception {
        PartnerInvoice invoice = invoice("INV-002");
        invoice.setPartnerId(7L);
        when(invoiceService.getInvoicesByPartner(7L)).thenReturn(List.of(invoice));

        mockMvc.perform(get("/api/partner-billing/invoices/partner/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].partnerId").value(7));
    }

    @Test
    void getInvoiceReturnsInvoice() throws Exception {
        when(invoiceService.getInvoiceById(1L)).thenReturn(invoice("INV-003"));

        mockMvc.perform(get("/api/partner-billing/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceNumber").value("INV-003"));
    }

    @Test
    void generateInvoiceReturnsSavedInvoice() throws Exception {
        when(invoiceService.generateInvoice(any(PartnerInvoice.class))).thenReturn(invoice("INV-004"));

        mockMvc.perform(post("/api/partner-billing/invoices/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"invoiceNumber\":\"INV-004\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceNumber").value("INV-004"));
    }

    @Test
    void markAsPaidReturnsPaidInvoice() throws Exception {
        PartnerInvoice invoice = invoice("INV-005");
        invoice.setStatus("PAID");
        when(invoiceService.markAsPaid(1L)).thenReturn(invoice);

        mockMvc.perform(put("/api/partner-billing/invoices/1/pay"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    void updateStatusReturnsUpdatedInvoice() throws Exception {
        PartnerInvoice invoice = invoice("INV-006");
        invoice.setStatus("CANCELLED");
        when(invoiceService.updateStatus(1L, "CANCELLED")).thenReturn(invoice);

        mockMvc.perform(put("/api/partner-billing/invoices/1/status")
                        .param("status", "CANCELLED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    private PartnerInvoice invoice(String number) {
        PartnerInvoice invoice = new PartnerInvoice();
        invoice.setId(1L);
        invoice.setInvoiceNumber(number);
        return invoice;
    }
}
