package tn.esprit.partnercontract.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.partnercontract.entities.PartnerContract;
import tn.esprit.partnercontract.repositories.PartnerContractRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartnerContractServiceTest {

    @Mock
    private PartnerContractRepository contractRepository;

    @InjectMocks
    private PartnerContractService contractService;

    private PartnerContract contract;

    @BeforeEach
    void setUp() {
        contract = new PartnerContract();
        contract.setId(1L);
        contract.setPartnerId(10L);
        contract.setTitle("Gold partner contract");
        contract.setStatus("DRAFT");
    }

    @Test
    void getAllContractsReturnsRepositoryData() {
        when(contractRepository.findAll()).thenReturn(List.of(contract));

        List<PartnerContract> result = contractService.getAllContracts();

        assertEquals(1, result.size());
        assertEquals("Gold partner contract", result.get(0).getTitle());
    }

    @Test
    void getContractsByPartnerFiltersByPartnerId() {
        when(contractRepository.findByPartnerId(10L)).thenReturn(List.of(contract));

        List<PartnerContract> result = contractService.getContractsByPartner(10L);

        assertEquals(10L, result.get(0).getPartnerId());
        verify(contractRepository).findByPartnerId(10L);
    }

    @Test
    void getContractByIdThrowsWhenMissing() {
        when(contractRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> contractService.getContractById(99L));
    }

    @Test
    void createContractSavesContract() {
        when(contractRepository.save(contract)).thenReturn(contract);

        PartnerContract result = contractService.createContract(contract);

        assertEquals("Gold partner contract", result.getTitle());
        verify(contractRepository).save(contract);
    }

    @Test
    void updateContractStatusChangesStatus() {
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));
        when(contractRepository.save(contract)).thenReturn(contract);

        PartnerContract result = contractService.updateContractStatus(1L, "ACTIVE");

        assertEquals("ACTIVE", result.getStatus());
        verify(contractRepository).save(contract);
    }

    @Test
    void deleteContractDeletesById() {
        contractService.deleteContract(1L);

        verify(contractRepository).deleteById(1L);
    }
}
