package com.example.service;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.entity.Pack;
import com.example.repository.PackRepository;

@ExtendWith(MockitoExtension.class)
public class PackServiceTest {

    @Mock
    private PackRepository packRepository;

    @InjectMocks
    private PackService packService;

    private Pack testPack;

    @BeforeEach
    public void setUp() {
        testPack = new Pack("Basic Pack", "Description1", 12, true);
        testPack.setId(1L);
    }

    @Test
    public void testGetAllPacks() {
        when(packRepository.findAll()).thenReturn(Arrays.asList(testPack));

        var result = packService.getAllPacks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Basic Pack", result.get(0).getName());
        verify(packRepository, times(1)).findAll();
    }

    @Test
    public void testGetPackById_Found() {
        when(packRepository.findById(1L)).thenReturn(Optional.of(testPack));

        var result = packService.getPackById(1L);

        assertTrue(result.isPresent());
        assertEquals("Basic Pack", result.get().getName());
        verify(packRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetPackById_NotFound() {
        when(packRepository.findById(999L)).thenReturn(Optional.empty());

        var result = packService.getPackById(999L);

        assertFalse(result.isPresent());
        verify(packRepository, times(1)).findById(999L);
    }

    @Test
    public void testCreatePack() {
        when(packRepository.save(any(Pack.class))).thenReturn(testPack);

        var result = packService.createPack(testPack);

        assertNotNull(result);
        assertEquals("Basic Pack", result.getName());
        verify(packRepository, times(1)).save(any(Pack.class));
    }

    @Test
    public void testUpdatePack() {
        Pack updatedPack = new Pack("Premium Pack", "Updated Description", 24, false);
        when(packRepository.findById(1L)).thenReturn(Optional.of(testPack));
        when(packRepository.save(any(Pack.class))).thenReturn(updatedPack);

        var result = packService.updatePack(1L, updatedPack);

        assertNotNull(result);
        assertEquals("Premium Pack", result.getName());
        verify(packRepository, times(1)).findById(1L);
        verify(packRepository, times(1)).save(any(Pack.class));
    }

    @Test
    public void testDeletePack() {
        when(packRepository.existsById(1L)).thenReturn(true);
        doNothing().when(packRepository).deleteById(1L);

        packService.deletePack(1L);

        verify(packRepository, times(1)).existsById(1L);
        verify(packRepository, times(1)).deleteById(1L);
    }
}