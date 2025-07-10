package com.example.Service;

import com.example.Model.MantenimientoModel;
import com.example.Repository.MantenimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MantenimientoServiceTest {

    @Mock
    private MantenimientoRepository mantenimientoRepository;

    @InjectMocks
    private MantenimientoService mantenimientoService;

    private MantenimientoModel mantenimiento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mantenimiento = new MantenimientoModel();
        mantenimiento.setId(1L);
        mantenimiento.setFechaProgramada(LocalDateTime.now().plusDays(2));
        mantenimiento.setTipo("Preventivo");
    }

    @Test
    void testFindByIdFound() {
        when(mantenimientoRepository.findById(1L)).thenReturn(Optional.of(mantenimiento));

        MantenimientoModel result = mantenimientoService.findById(1L);

        assertNotNull(result);
        assertEquals("Preventivo", result.getTipo());
        verify(mantenimientoRepository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(mantenimientoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> mantenimientoService.findById(2L));
    }

    @Test
    void testSave() {
        when(mantenimientoRepository.save(mantenimiento)).thenReturn(mantenimiento);

        MantenimientoModel result = mantenimientoService.save(mantenimiento);

        assertEquals("Preventivo", result.getTipo());
        verify(mantenimientoRepository).save(mantenimiento);
    }

    @Test
    void testDeleteById() {
        when(mantenimientoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(mantenimientoRepository).deleteById(1L);

        mantenimientoService.deleteById(1L);

        verify(mantenimientoRepository).deleteById(1L);
    }

    @Test
    void testDeleteByIdNotFound() {
        when(mantenimientoRepository.existsById(2L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> mantenimientoService.deleteById(2L));
    }
}