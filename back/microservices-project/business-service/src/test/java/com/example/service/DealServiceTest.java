package com.example.service;

import com.example.entity.Deal;
import com.example.repository.DealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DealServiceTest {

    @Mock
    private DealRepository dealRepository;

    @InjectMocks
    private DealService dealService;

    private Deal testDeal;

    @BeforeEach
    public void setUp() {
        testDeal = new Deal("Summer Deal", "50% off", 1L, LocalDate.now(), LocalDate.now().plusDays(30));
        testDeal.setId(1L);
    }

    @Test
    public void testGetAllDeals() {
        when(dealRepository.findAll()).thenReturn(Arrays.asList(testDeal));

        var result = dealService.getAllDeals();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Summer Deal", result.get(0).getTitle());
        verify(dealRepository, times(1)).findAll();
    }

    @Test
    public void testGetDealById_Found() {
        when(dealRepository.findById(1L)).thenReturn(Optional.of(testDeal));

        var result = dealService.getDealById(1L);

        assertTrue(result.isPresent());
        assertEquals("Summer Deal", result.get().getTitle());
        verify(dealRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetDealById_NotFound() {
        when(dealRepository.findById(999L)).thenReturn(Optional.empty());

        var result = dealService.getDealById(999L);

        assertFalse(result.isPresent());
        verify(dealRepository, times(1)).findById(999L);
    }

    @Test
    public void testCreateDeal() {
        when(dealRepository.save(any(Deal.class))).thenReturn(testDeal);

        var result = dealService.createDeal(testDeal);

        assertNotNull(result);
        assertEquals("Summer Deal", result.getTitle());
        verify(dealRepository, times(1)).save(any(Deal.class));
    }

    @Test
    public void testUpdateDeal() {
        Deal updatedDeal = new Deal("Winter Deal", "30% off", 1L, LocalDate.now(), LocalDate.now().plusDays(60));
        when(dealRepository.findById(1L)).thenReturn(Optional.of(testDeal));
        when(dealRepository.save(any(Deal.class))).thenReturn(updatedDeal);

        var result = dealService.updateDeal(1L, updatedDeal);

        assertNotNull(result);
        assertEquals("Winter Deal", result.getTitle());
        verify(dealRepository, times(1)).findById(1L);
        verify(dealRepository, times(1)).save(any(Deal.class));
    }

    @Test
    public void testDeleteDeal() {
        when(dealRepository.existsById(1L)).thenReturn(true);
        doNothing().when(dealRepository).deleteById(1L);

        dealService.deleteDeal(1L);

        verify(dealRepository, times(1)).existsById(1L);
        verify(dealRepository, times(1)).deleteById(1L);
    }
}