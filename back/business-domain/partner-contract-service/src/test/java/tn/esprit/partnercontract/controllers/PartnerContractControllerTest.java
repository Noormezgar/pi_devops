package tn.esprit.partnercontract.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.partnercontract.entities.PartnerContract;
import tn.esprit.partnercontract.services.PartnerContractService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PartnerContractController.class)
class PartnerContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartnerContractService contractService;

    @Test
    void getAllContractsReturnsContracts() throws Exception {
        PartnerContract contract = contract("Gold contract");
        when(contractService.getAllContracts()).thenReturn(List.of(contract));

        mockMvc.perform(get("/api/partner-contracts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Gold contract"));
    }

    @Test
    void getContractsByPartnerReturnsContracts() throws Exception {
        PartnerContract contract = contract("Partner contract");
        contract.setPartnerId(10L);
        when(contractService.getContractsByPartner(10L)).thenReturn(List.of(contract));

        mockMvc.perform(get("/api/partner-contracts/partner/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].partnerId").value(10));
    }

    @Test
    void createContractReturnsCreatedContract() throws Exception {
        when(contractService.createContract(any(PartnerContract.class))).thenReturn(contract("Created contract"));

        mockMvc.perform(post("/api/partner-contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Created contract\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Created contract"));
    }

    @Test
    void updateStatusReturnsUpdatedContract() throws Exception {
        PartnerContract contract = contract("Gold contract");
        contract.setStatus("ACTIVE");
        when(contractService.updateContractStatus(1L, "ACTIVE")).thenReturn(contract);

        mockMvc.perform(put("/api/partner-contracts/1/status").param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void deleteContractReturnsOk() throws Exception {
        doNothing().when(contractService).deleteContract(1L);

        mockMvc.perform(delete("/api/partner-contracts/1"))
                .andExpect(status().isOk());
    }

    private PartnerContract contract(String title) {
        PartnerContract contract = new PartnerContract();
        contract.setId(1L);
        contract.setTitle(title);
        return contract;
    }
}
