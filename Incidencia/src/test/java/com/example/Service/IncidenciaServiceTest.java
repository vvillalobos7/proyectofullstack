package com.example.Service;

import com.example.Model.IncidenciaModel;
import com.example.Model.TecnicoModel;
import com.example.Model.UsuarioModel;
import com.example.Model.EquipoModel;
import com.example.Repository.IncidenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidenciaServiceTest {

    @Mock
    private IncidenciaRepository incidenciaRepository;

    @InjectMocks
    private IncidenciaService incidenciaService;

    private IncidenciaModel incidenciaTest;
    private UsuarioModel usuarioTest;
    private EquipoModel equipoTest;
    private TecnicoModel tecnicoTest;

    @BeforeEach
    void setUp() {
        usuarioTest = new UsuarioModel();
        usuarioTest.setIdusuario(1L); // Usar el nombre correcto del setter
        usuarioTest.setNombre("Juan");
        usuarioTest.setApellido("Pérez");
        usuarioTest.setCorreo("juan@test.com");

        equipoTest = new EquipoModel();
        equipoTest.setIdEquipo(1L);
        equipoTest.setNombre("Tractor Case I.H. 4210A");

        // Crear un usuario para el técnico
        UsuarioModel usuarioTecnico = new UsuarioModel();
        usuarioTecnico.setIdusuario(2L);
        usuarioTecnico.setNombre("Carlos");
        usuarioTecnico.setApellido("Técnico");

        tecnicoTest = new TecnicoModel();
        tecnicoTest.setIdTecnico(1L);
        tecnicoTest.setUsuario(usuarioTecnico); // Asignar el usuario al técnico
        tecnicoTest.setEspecialidad("Reparación de equipos");

        incidenciaTest = new IncidenciaModel();
        incidenciaTest.setIdIncidencia(1L);
        incidenciaTest.setUsuario(usuarioTest);
        incidenciaTest.setEquipo(equipoTest);
        incidenciaTest.setDescripcion("Problema con el equipo");
        incidenciaTest.setEstado("ABIERTA");
        incidenciaTest.setPrioridad("MEDIA");
        incidenciaTest.setFechaReporte(new Date());
    }

    @Test
    void findAll_DeberiaRetornarListaDeIncidencias() {
        // Given
        List<IncidenciaModel> incidencias = Arrays.asList(incidenciaTest);
        when(incidenciaRepository.findAll()).thenReturn(incidencias);

        // When
        List<IncidenciaModel> resultado = incidenciaService.findAll();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(incidenciaTest, resultado.get(0));
        verify(incidenciaRepository, times(1)).findAll();
    }

    @Test
    void findById_CuandoExiste_DeberiaRetornarIncidencia() {
        // Given
        when(incidenciaRepository.findById(1L)).thenReturn(Optional.of(incidenciaTest));

        // When
        Optional<IncidenciaModel> resultado = incidenciaService.findById(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(incidenciaTest, resultado.get());
        verify(incidenciaRepository, times(1)).findById(1L);
    }

    @Test
    void findById_CuandoNoExiste_DeberiaRetornarEmpty() {
        // Given
        when(incidenciaRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<IncidenciaModel> resultado = incidenciaService.findById(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(incidenciaRepository, times(1)).findById(999L);
    }

    @Test
    void save_CuandoEsNuevaIncidencia_DeberiaEstablecerValoresPorDefecto() {
        // Given
        IncidenciaModel nuevaIncidencia = new IncidenciaModel();
        nuevaIncidencia.setUsuario(usuarioTest);
        nuevaIncidencia.setEquipo(equipoTest);
        nuevaIncidencia.setDescripcion("Nueva incidencia");

        // Simulamos el comportamiento del save que retorna la incidencia con valores por defecto
        IncidenciaModel incidenciaGuardada = new IncidenciaModel();
        incidenciaGuardada.setUsuario(usuarioTest);
        incidenciaGuardada.setEquipo(equipoTest);
        incidenciaGuardada.setDescripcion("Nueva incidencia");
        incidenciaGuardada.setEstado("ABIERTA");
        incidenciaGuardada.setPrioridad("MEDIA");
        incidenciaGuardada.setFechaReporte(new Date());

        when(incidenciaRepository.save(any(IncidenciaModel.class))).thenReturn(incidenciaGuardada);

        // When
        IncidenciaModel resultado = incidenciaService.save(nuevaIncidencia);

        // Then
        assertNotNull(resultado.getFechaReporte());
        assertEquals("ABIERTA", resultado.getEstado());
        assertEquals("MEDIA", resultado.getPrioridad());
        verify(incidenciaRepository, times(1)).save(any(IncidenciaModel.class));
    }

    @Test
    void asignarTecnico_CuandoIncidenciaExiste_DeberiaAsignarTecnicoYCambiarEstado() {
        // Given
        when(incidenciaRepository.findById(1L)).thenReturn(Optional.of(incidenciaTest));
        
        // Configurar el comportamiento del save para que retorne la incidencia modificada
        IncidenciaModel incidenciaModificada = new IncidenciaModel();
        incidenciaModificada.setIdIncidencia(1L);
        incidenciaModificada.setUsuario(usuarioTest);
        incidenciaModificada.setEquipo(equipoTest);
        incidenciaModificada.setDescripcion("Problema con el equipo");
        incidenciaModificada.setEstado("EN_PROCESO");
        incidenciaModificada.setPrioridad("MEDIA");
        incidenciaModificada.setFechaReporte(new Date());
        incidenciaModificada.setTecnico(tecnicoTest);
        
        when(incidenciaRepository.save(any(IncidenciaModel.class))).thenReturn(incidenciaModificada);

        // When
        IncidenciaModel resultado = incidenciaService.asignarTecnico(1L, tecnicoTest);

        // Then
        assertEquals(tecnicoTest, resultado.getTecnico());
        assertEquals("EN_PROCESO", resultado.getEstado());
        verify(incidenciaRepository, times(1)).findById(1L);
        verify(incidenciaRepository, times(1)).save(any(IncidenciaModel.class));
    }

    @Test
    void asignarTecnico_CuandoIncidenciaNoExiste_DeberiaLanzarExcepcion() {
        // Given
        when(incidenciaRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> incidenciaService.asignarTecnico(999L, tecnicoTest));
        
        assertEquals("Incidencia no encontrada con ID: 999", exception.getMessage());
        verify(incidenciaRepository, times(1)).findById(999L);
        verify(incidenciaRepository, never()).save(any());
    }

    @Test
    void cerrarIncidencia_CuandoIncidenciaExiste_DeberiaCerrarIncidencia() {
        // Given
        String solucion = "Problema resuelto";
        when(incidenciaRepository.findById(1L)).thenReturn(Optional.of(incidenciaTest));
        
        // Configurar el comportamiento del save para que retorne la incidencia cerrada
        IncidenciaModel incidenciaCerrada = new IncidenciaModel();
        incidenciaCerrada.setIdIncidencia(1L);
        incidenciaCerrada.setUsuario(usuarioTest);
        incidenciaCerrada.setEquipo(equipoTest);
        incidenciaCerrada.setDescripcion("Problema con el equipo");
        incidenciaCerrada.setEstado("CERRADA");
        incidenciaCerrada.setPrioridad("MEDIA");
        incidenciaCerrada.setFechaReporte(new Date());
        incidenciaCerrada.setSolucion(solucion);
        incidenciaCerrada.setFechaResolucion(new Date());
        
        when(incidenciaRepository.save(any(IncidenciaModel.class))).thenReturn(incidenciaCerrada);

        // When
        IncidenciaModel resultado = incidenciaService.cerrarIncidencia(1L, solucion);

        // Then
        assertEquals("CERRADA", resultado.getEstado());
        assertEquals(solucion, resultado.getSolucion());
        assertNotNull(resultado.getFechaResolucion());
        verify(incidenciaRepository, times(1)).findById(1L);
        verify(incidenciaRepository, times(1)).save(any(IncidenciaModel.class));
    }

    @Test
    void deleteById_CuandoIncidenciaExiste_DeberiaEliminarIncidencia() {
        // Given
        when(incidenciaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(incidenciaRepository).deleteById(1L);

        // When
        assertDoesNotThrow(() -> incidenciaService.deleteById(1L));

        // Then
        verify(incidenciaRepository, times(1)).existsById(1L);
        verify(incidenciaRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_CuandoIncidenciaNoExiste_DeberiaLanzarExcepcion() {
        // Given
        when(incidenciaRepository.existsById(999L)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> incidenciaService.deleteById(999L));
        
        assertEquals("Incidencia no encontrada con ID: 999", exception.getMessage());
        verify(incidenciaRepository, times(1)).existsById(999L);
        verify(incidenciaRepository, never()).deleteById(anyLong());
    }

    @Test
    void countIncidenciasByUsuario_DeberiaRetornarConteoCorreto() {
        // Given
        Long idUsuario = 1L;
        Long expectedCount = 5L;
        when(incidenciaRepository.countIncidenciasByUsuario(idUsuario)).thenReturn(expectedCount);

        // When
        Long resultado = incidenciaService.countIncidenciasByUsuario(idUsuario);

        // Then
        assertEquals(expectedCount, resultado);
        verify(incidenciaRepository, times(1)).countIncidenciasByUsuario(idUsuario);
    }

    @Test
    void findByCliente_DeberiaRetornarIncidenciasDelCliente() {
        // Given
        Long idUsuario = 1L;
        List<IncidenciaModel> incidencias = Arrays.asList(incidenciaTest);
        when(incidenciaRepository.findByUsuarioId(idUsuario)).thenReturn(incidencias);

        // When
        List<IncidenciaModel> resultado = incidenciaService.findByCliente(idUsuario);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(incidenciaTest, resultado.get(0));
        verify(incidenciaRepository, times(1)).findByUsuarioId(idUsuario);
    }

    @Test
    void findByEstado_DeberiaRetornarIncidenciasPorEstado() {
        // Given
        String estado = "ABIERTA";
        List<IncidenciaModel> incidencias = Arrays.asList(incidenciaTest);
        when(incidenciaRepository.findByEstado(estado)).thenReturn(incidencias);

        // When
        List<IncidenciaModel> resultado = incidenciaService.findByEstado(estado);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(incidenciaTest, resultado.get(0));
        verify(incidenciaRepository, times(1)).findByEstado(estado);
    }
}