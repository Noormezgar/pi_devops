package com.example.service;

import com.example.entity.Partner;
import com.example.repository.PartnerRepository;
import com.example.repository.DealRepository;
import com.example.repository.AccessCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PartnerServiceTest {

    @Mock
    private PartnerRepository partnerRepository;

    @Mock
    private DealRepository dealRepository;

    @Mock
    private AccessCodeRepository accessCodeRepository;

    @InjectMocks
    private PartnerService partnerService;

    private Partner testPartner;

    @BeforeEach
    public void setUp() {
        testPartner = new Partner("TestPartner", "test@partner.com", "123456");
        testPartner.setId(1L);
    }

    @Test
    public void testGetAllPartners() {
        when(partnerRepository.findAll()).thenReturn(Arrays.asList(testPartner));

        var result = partnerService.getAllPartners();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TestPartner", result.get(0).getName());
        verify(partnerRepository, times(1)).findAll();
    }

    @Test
    public void testGetPartnerById_Found() {
        when(partnerRepository.findById(1L)).thenReturn(Optional.of(testPartner));

        var result = partnerService.getPartnerById(1L);

        assertTrue(result.isPresent());
        assertEquals("TestPartner", result.get().getName());
        verify(partnerRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetPartnerById_NotFound() {
        when(partnerRepository.findById(999L)).thenReturn(Optional.empty());

        var result = partnerService.getPartnerById(999L);

        assertFalse(result.isPresent());
        verify(partnerRepository, times(1)).findById(999L);
    }

    @Test
    public void testCreatePartner() {
        when(partnerRepository.save(any(Partner.class))).thenReturn(testPartner);

        var result = partnerService.createPartner(testPartner);

        assertNotNull(result);
        assertEquals("TestPartner", result.getName());
        verify(partnerRepository, times(1)).save(any(Partner.class));
    }

    @Test
    public void testUpdatePartner() {
        Partner updatedPartner = new Partner("UpdatedPartner", "updated@partner.com", "789012");
        when(partnerRepository.findById(1L)).thenReturn(Optional.of(testPartner));
        when(partnerRepository.save(any(Partner.class))).thenReturn(updatedPartner);

        var result = partnerService.updatePartner(1L, updatedPartner);

        assertNotNull(result);
        assertEquals("UpdatedPartner", result.getName());
        verify(partnerRepository, times(1)).findById(1L);
        verify(partnerRepository, times(1)).save(any(Partner.class));
    }

    @Test
    public void testDeletePartner() {
        when(partnerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(partnerRepository).deleteById(1L);

        partnerService.deletePartner(1L);

        verify(partnerRepository, times(1)).existsById(1L);
        verify(partnerRepository, times(1)).deleteById(1L);
    }
}