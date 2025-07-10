package com.example.Service;

import com.example.Model.EntregaModel;
import com.example.Repository.EntregaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntregaServiceTest {

    @Mock
    private EntregaRepository entregaRepository;

    @InjectMocks
    private EntregaService entregaService;

    private EntregaModel entrega;

    @BeforeEach
    void setUp() {
        entrega = new EntregaModel();
        entrega.setIdEntrega(1L); // Suponiendo que el campo real es idEntrega
        entrega.setEstado("PROGRAMADA");
        entrega.setFechaProgramada(LocalDateTime.now());
        entrega.setCiudad("Santiago");
        entrega.setTransportista("Juan Perez");
        entrega.setCosto(1000.0);
        entrega.setTipoEntrega("URGENTE");
    }

    @Test
    void testFindByIdSuccess() {
        when(entregaRepository.findById(1L)).thenReturn(Optional.of(entrega));
        EntregaModel result = entregaService.findById(1L);
        assertNotNull(result);
        assertEquals("PROGRAMADA", result.getEstado());
        verify(entregaRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(entregaRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> entregaService.findById(1L));
        assertEquals("Entrega no encontrada con ID: 1", thrown.getMessage());
    }

    @Test
    void testFindByEstado() {
        when(entregaRepository.findByEstado("PROGRAMADA")).thenReturn(List.of(entrega));
        List<EntregaModel> result = entregaService.findByEstado("PROGRAMADA");
        assertEquals(1, result.size());
    }

    @Test
    void testSave() {
        when(entregaRepository.save(entrega)).thenReturn(entrega);
        EntregaModel result = entregaService.save(entrega);
        assertEquals("PROGRAMADA", result.getEstado());
        verify(entregaRepository, times(1)).save(entrega);
    }

    @Test
    void testDeleteByIdSuccess() {
        when(entregaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(entregaRepository).deleteById(1L);
        entregaService.deleteById(1L);
        verify(entregaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteByIdNotFound() {
        when(entregaRepository.existsById(1L)).thenReturn(false);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> entregaService.deleteById(1L));
        assertEquals("Entrega no encontrada con ID: 1", thrown.getMessage());
    }

    @Test
    void testFindEntregasActivasTransportista() {
        when(entregaRepository.findByTransportistaAndEstadoIn(eq("Juan Perez"), anyList()))
                .thenReturn(List.of(entrega));
        List<EntregaModel> result = entregaService.findEntregasActivasTransportista("Juan Perez");
        assertEquals(1, result.size());
        assertEquals("Juan Perez", result.get(0).getTransportista());
    }

    @Test
    void testContarPorEstado() {
        when(entregaRepository.countByEstado("PROGRAMADA")).thenReturn(5L);
        Long count = entregaService.contarPorEstado("PROGRAMADA");
        assertEquals(5L, count);
    }

    @Test
    void testGetCiudadesDisponibles() {
        when(entregaRepository.findDistinctCiudades()).thenReturn(Arrays.asList("Santiago", "Valparaíso"));
        List<String> ciudades = entregaService.getCiudadesDisponibles();
        assertEquals(2, ciudades.size());
        assertTrue(ciudades.contains("Santiago"));
    }

    @Test
    void testValidarDisponibilidadTransportista_Disponible() {
        when(entregaRepository.findByTransportistaAndEstadoIn(eq("Juan Perez"), anyList()))
                .thenReturn(List.of());
        boolean disponible = entregaService.validarDisponibilidadTransportista("Juan Perez", LocalDateTime.now());
        assertTrue(disponible);
    }

    @Test
    void testValidarDisponibilidadTransportista_NoDisponible() {
        EntregaModel entregaHoy = new EntregaModel();
        entregaHoy.setFechaProgramada(LocalDateTime.now());
        when(entregaRepository.findByTransportistaAndEstadoIn(eq("Juan Perez"), anyList()))
                .thenReturn(List.of(entregaHoy));
        boolean disponible = entregaService.validarDisponibilidadTransportista("Juan Perez", LocalDateTime.now());
        assertFalse(disponible);
    }

    @Test
    void testCambiarEstadoConFechaRealizada() {
        when(entregaRepository.findById(1L)).thenReturn(Optional.of(entrega));
        when(entregaRepository.save(any(EntregaModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EntregaModel result = entregaService.cambiarEstado(1L, "ENTREGADA");

        assertEquals("ENTREGADA", result.getEstado());
        assertNotNull(result.getFechaRealizada());
        verify(entregaRepository).save(result);
    }

    @Test
    void testSaveEntregaConFechaCreacion() {
        // Probar que se establece correctamente la fecha de creación
        when(entregaRepository.save(entrega)).thenReturn(entrega);
        entregaService.save(entrega);
        assertNotNull(entrega.getFechaCreacion()); // Asegurarse que la fecha de creación no es null
    }
}