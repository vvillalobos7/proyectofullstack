package com.example.Seguimiento.Service;

import com.example.Seguimiento.Model.Seguimiento;
import com.example.Seguimiento.Repository.SeguimientoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SeguimientoServiceTest {

    @Mock
    private SeguimientoRepository seguimientoRepository;

    @InjectMocks
    private SeguimientoService seguimientoService;

    private Seguimiento seguimiento;

    @BeforeEach
    void setUp() {
        seguimiento = new Seguimiento(1L, "Revisión inicial", "PENDIENTE", LocalDateTime.now());
    }

    @Test
    void testObtenerTodos() {
        when(seguimientoRepository.findAll()).thenReturn(List.of(seguimiento));
        List<Seguimiento> result = seguimientoService.obtenerTodos();
        assertEquals(1, result.size());
        verify(seguimientoRepository).findAll();
    }

    @Test
    void testGuardar() {
        when(seguimientoRepository.save(seguimiento)).thenReturn(seguimiento);
        Seguimiento result = seguimientoService.guardar(seguimiento);
        assertNotNull(result);
        assertEquals("Revisión inicial", result.getDescripcion());
        verify(seguimientoRepository).save(seguimiento);
    }

    @Test
    void testObtenerPorIdExistente() {
        when(seguimientoRepository.findById(1L)).thenReturn(Optional.of(seguimiento));
        Seguimiento result = seguimientoService.obtenerPorId(1L);
        assertNotNull(result);
        assertEquals("PENDIENTE", result.getEstado());
    }

    @Test
    void testObtenerPorIdInexistente() {
        when(seguimientoRepository.findById(1L)).thenReturn(Optional.empty());
        Seguimiento result = seguimientoService.obtenerPorId(1L);
        assertNull(result);
    }

    @Test
    void testEliminar() {
        doNothing().when(seguimientoRepository).deleteById(1L);
        seguimientoService.eliminar(1L);
        verify(seguimientoRepository).deleteById(1L);
    }
}