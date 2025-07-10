package com.example.Controller;

import com.example.Model.IncidenciaModel;
import com.example.Model.TecnicoModel;
import com.example.Model.UsuarioModel;
import com.example.Model.EquipoModel;
import com.example.Service.IncidenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IncidenciaController.class)
class IncidenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncidenciaService incidenciaService;

    private ObjectMapper objectMapper;

    private IncidenciaModel incidenciaTest;
    private UsuarioModel usuarioTest;
    private EquipoModel equipoTest;
    private TecnicoModel tecnicoTest;

    @BeforeEach
    void setUp() {
        // Configurar ObjectMapper para manejar fechas correctamente
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

        // Configurar datos de prueba
        usuarioTest = new UsuarioModel();
        usuarioTest.setIdusuario(1L);
        usuarioTest.setNombre("Juan");
        usuarioTest.setApellido("Pérez");
        usuarioTest.setCorreo("juan@test.com");
        usuarioTest.setContraseña("password123");
        usuarioTest.setTelefono("123456789");
        usuarioTest.setIdperfil(1L);

        equipoTest = new EquipoModel();
        equipoTest.setIdEquipo(1L);
        equipoTest.setNombre("Tractor Case I.H. 4210A");
        equipoTest.setDescripcion("TRACTOR");
        equipoTest.setMarca("CASE");
        equipoTest.setModelo("4210A");
        equipoTest.setEstado("ACTIVO");
        equipoTest.setPrecioventa(999.99);
        equipoTest.setPatente("ABC123");

        // Crear un usuario para el técnico
        UsuarioModel usuarioTecnico = new UsuarioModel();
        usuarioTecnico.setIdusuario(2L);
        usuarioTecnico.setNombre("Carlos");
        usuarioTecnico.setApellido("Técnico");
        usuarioTecnico.setCorreo("carlos@test.com");
        usuarioTecnico.setContraseña("password456");
        usuarioTecnico.setTelefono("987654321");
        usuarioTecnico.setIdperfil(2L);

        tecnicoTest = new TecnicoModel();
        tecnicoTest.setIdTecnico(1L);
        tecnicoTest.setUsuario(usuarioTecnico);
        tecnicoTest.setEspecialidad("Reparación de equipos");
        tecnicoTest.setDisponibilidad(true);
        tecnicoTest.setZonaCobertura("Norte");

        incidenciaTest = new IncidenciaModel();
        incidenciaTest.setIdIncidencia(1L);
        incidenciaTest.setUsuario(usuarioTest);
        incidenciaTest.setEquipo(equipoTest);
        incidenciaTest.setDescripcion("Problema con el equipo");
        incidenciaTest.setEstado("ABIERTA");
        incidenciaTest.setPrioridad("MEDIA");
        incidenciaTest.setFechaReporte(new Date());
        incidenciaTest.setFechaCreacion(new Date());
    }

    @Test
    void getAllIncidencias_DeberiaRetornarListaDeIncidencias() throws Exception {
        // Given
        List<IncidenciaModel> incidencias = Arrays.asList(incidenciaTest);
        when(incidenciaService.findAll()).thenReturn(incidencias);

        // When & Then
        mockMvc.perform(get("/api/v1/incidencias")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idIncidencia").value(1L))
                .andExpect(jsonPath("$[0].descripcion").value("Problema con el equipo"))
                .andExpect(jsonPath("$[0].estado").value("ABIERTA"));

        verify(incidenciaService, times(1)).findAll();
    }

    @Test
    void getIncidenciaById_CuandoExiste_DeberiaRetornarIncidencia() throws Exception {
        // Given
        when(incidenciaService.findById(1L)).thenReturn(Optional.of(incidenciaTest));

        // When & Then
        mockMvc.perform(get("/api/v1/incidencias/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idIncidencia").value(1L))
                .andExpect(jsonPath("$.descripcion").value("Problema con el equipo"))
                .andExpect(jsonPath("$.estado").value("ABIERTA"));

        verify(incidenciaService, times(1)).findById(1L);
    }

    @Test
    void getIncidenciaById_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        // Given
        when(incidenciaService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/incidencias/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(incidenciaService, times(1)).findById(999L);
    }

    @Test
    void getIncidenciasByCliente_DeberiaRetornarIncidenciasDelCliente() throws Exception {
        // Given
        List<IncidenciaModel> incidencias = Arrays.asList(incidenciaTest);
        when(incidenciaService.findByCliente(1L)).thenReturn(incidencias);

        // When & Then
        mockMvc.perform(get("/api/v1/incidencias/cliente/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idIncidencia").value(1L));

        verify(incidenciaService, times(1)).findByCliente(1L);
    }

    @Test
    void getIncidenciasByEquipo_DeberiaRetornarIncidenciasDelEquipo() throws Exception {
        // Given
        List<IncidenciaModel> incidencias = Arrays.asList(incidenciaTest);
        when(incidenciaService.findByEquipo(1L)).thenReturn(incidencias);

        // When & Then
        mockMvc.perform(get("/api/v1/incidencias/equipo/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idIncidencia").value(1L));

        verify(incidenciaService, times(1)).findByEquipo(1L);
    }

    @Test
    void getIncidenciasByTecnico_DeberiaRetornarIncidenciasDelTecnico() throws Exception {
        // Given
        List<IncidenciaModel> incidencias = Arrays.asList(incidenciaTest);
        when(incidenciaService.findByTecnico(1L)).thenReturn(incidencias);

        // When & Then
        mockMvc.perform(get("/api/v1/incidencias/tecnico/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idIncidencia").value(1L));

        verify(incidenciaService, times(1)).findByTecnico(1L);
    }

    @Test
    void createIncidencia_ConDatosValidos_DeberiaCrearIncidencia() throws Exception {
        // Given
        IncidenciaModel nuevaIncidencia = new IncidenciaModel();
        nuevaIncidencia.setUsuario(usuarioTest);
        nuevaIncidencia.setEquipo(equipoTest);
        nuevaIncidencia.setDescripcion("Nueva incidencia");

        when(incidenciaService.save(any(IncidenciaModel.class))).thenReturn(incidenciaTest);

        // When & Then
        mockMvc.perform(post("/api/v1/incidencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevaIncidencia)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idIncidencia").value(1L));

        verify(incidenciaService, times(1)).save(any(IncidenciaModel.class));
    }

    @Test
    void updateIncidencia_CuandoExiste_DeberiaActualizarIncidencia() throws Exception {
        // Given
        when(incidenciaService.findById(1L)).thenReturn(Optional.of(incidenciaTest));
        
        // Crear incidencia actualizada
        IncidenciaModel incidenciaActualizada = new IncidenciaModel();
        incidenciaActualizada.setIdIncidencia(1L);
        incidenciaActualizada.setUsuario(usuarioTest);
        incidenciaActualizada.setEquipo(equipoTest);
        incidenciaActualizada.setDescripcion("Descripción actualizada");
        incidenciaActualizada.setEstado("EN_PROCESO");
        incidenciaActualizada.setPrioridad("ALTA");
        incidenciaActualizada.setFechaReporte(new Date());
        incidenciaActualizada.setFechaCreacion(new Date());

        when(incidenciaService.save(any(IncidenciaModel.class))).thenReturn(incidenciaActualizada);

        IncidenciaModel requestBody = new IncidenciaModel();
        requestBody.setDescripcion("Descripción actualizada");
        requestBody.setEstado("EN_PROCESO");
        requestBody.setPrioridad("ALTA");

        // When & Then
        mockMvc.perform(put("/api/v1/incidencias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(incidenciaService, times(1)).findById(1L);
        verify(incidenciaService, times(1)).save(any(IncidenciaModel.class));
    }

    @Test
    void updateIncidencia_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        // Given
        when(incidenciaService.findById(999L)).thenReturn(Optional.empty());

        IncidenciaModel incidenciaActualizada = new IncidenciaModel();
        incidenciaActualizada.setDescripcion("Descripción actualizada");

        // When & Then
        mockMvc.perform(put("/api/v1/incidencias/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incidenciaActualizada)))
                .andExpect(status().isNotFound());

        verify(incidenciaService, times(1)).findById(999L);
        verify(incidenciaService, never()).save(any(IncidenciaModel.class));
    }

    @Test
    void deleteIncidencia_CuandoExiste_DeberiaEliminarIncidencia() throws Exception {
        // Given
        when(incidenciaService.findById(1L)).thenReturn(Optional.of(incidenciaTest));
        doNothing().when(incidenciaService).deleteById(1L);

        // When & Then
        mockMvc.perform(delete("/api/v1/incidencias/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(incidenciaService, times(1)).findById(1L);
        verify(incidenciaService, times(1)).deleteById(1L);
    }

    @Test
    void deleteIncidencia_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        // Given
        when(incidenciaService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(delete("/api/v1/incidencias/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(incidenciaService, times(1)).findById(999L);
        verify(incidenciaService, never()).deleteById(anyLong());
    }

    @Test
    void asignarTecnico_CuandoIncidenciaExiste_DeberiaAsignarTecnico() throws Exception {
        // Given
        IncidenciaModel incidenciaConTecnico = new IncidenciaModel();
        incidenciaConTecnico.setIdIncidencia(1L);
        incidenciaConTecnico.setUsuario(usuarioTest);
        incidenciaConTecnico.setEquipo(equipoTest);
        incidenciaConTecnico.setDescripcion("Problema con el equipo");
        incidenciaConTecnico.setEstado("EN_PROCESO");
        incidenciaConTecnico.setPrioridad("MEDIA");
        incidenciaConTecnico.setFechaReporte(new Date());
        incidenciaConTecnico.setFechaCreacion(new Date());
        incidenciaConTecnico.setTecnico(tecnicoTest);

        when(incidenciaService.asignarTecnico(eq(1L), any(TecnicoModel.class)))
                .thenReturn(incidenciaConTecnico);

        // When & Then
        mockMvc.perform(put("/api/v1/incidencias/1/asignar-tecnico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tecnicoTest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idIncidencia").value(1L))
                .andExpect(jsonPath("$.estado").value("EN_PROCESO"));

        verify(incidenciaService, times(1)).asignarTecnico(eq(1L), any(TecnicoModel.class));
    }

    @Test
    void asignarTecnico_CuandoIncidenciaNoExiste_DeberiaRetornar404() throws Exception {
        // Given
        when(incidenciaService.asignarTecnico(eq(999L), any(TecnicoModel.class)))
                .thenThrow(new RuntimeException("Incidencia no encontrada"));

        // When & Then
        mockMvc.perform(put("/api/v1/incidencias/999/asignar-tecnico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tecnicoTest)))
                .andExpect(status().isNotFound());

        verify(incidenciaService, times(1)).asignarTecnico(eq(999L), any(TecnicoModel.class));
    }

    @Test
    void getIncidenciasByFechaReporte_DeberiaRetornarIncidenciasEnRangoFechas() throws Exception {
        // Given
        List<IncidenciaModel> incidencias = Arrays.asList(incidenciaTest);
        Date fechaInicio = new Date();
        Date fechaFin = new Date();
        
        when(incidenciaService.findByFechaReporte(any(Date.class), any(Date.class)))
                .thenReturn(incidencias);

        // When & Then
        mockMvc.perform(get("/api/v1/incidencias/fecha")
                .param("inicio", "2024-01-01")
                .param("fin", "2024-12-31")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idIncidencia").value(1L));

        verify(incidenciaService, times(1)).findByFechaReporte(any(Date.class), any(Date.class));
    }
}