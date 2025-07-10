package com.example.Controller;

import com.example.Model.EntregaModel;
import com.example.Service.EntregaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EntregaControllerTest {

    @Mock
    private EntregaService entregaService;

    @InjectMocks
    private EntregaController entregaController;

    private MockMvc mockMvc;

    private EntregaModel entrega;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(entregaController).build();
        entrega = new EntregaModel();
        entrega.setIdEntrega(1L);
        entrega.setEstado("PROGRAMADA");
        entrega.setFechaProgramada(LocalDateTime.now());
        entrega.setCiudad("Santiago");
        entrega.setTransportista("Juan Perez");
        entrega.setCosto(1000.0);
        entrega.setTipoEntrega("URGENTE");
    }

    @Test
    void testGetAllEntregas() throws Exception {
        List<EntregaModel> entregas = Arrays.asList(entrega);

        when(entregaService.findAll()).thenReturn(entregas);

        mockMvc.perform(get("/api/v1/entregas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].estado").value("PROGRAMADA"))
                .andExpect(jsonPath("$[0].transportista").value("Juan Perez"));
    }

    @Test
    void testGetEntregaByIdSuccess() throws Exception {
        when(entregaService.findById(1L)).thenReturn(entrega);

        mockMvc.perform(get("/api/v1/entregas/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.estado").value("PROGRAMADA"))
                .andExpect(jsonPath("$.transportista").value("Juan Perez"));
    }


    @Test
    void testCreateEntrega() throws Exception {
        when(entregaService.save(any(EntregaModel.class))).thenReturn(entrega);

        mockMvc.perform(post("/api/v1/entregas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\":\"PROGRAMADA\", \"fechaProgramada\":\"2025-07-09T10:00:00\", \"ciudad\":\"Santiago\", \"transportista\":\"Juan Perez\", \"costo\":1000.0, \"tipoEntrega\":\"URGENTE\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.estado").value("PROGRAMADA"));
    }


    @Test
    void testDeleteEntregaSuccess() throws Exception {
        doNothing().when(entregaService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/entregas/1"))
                .andExpect(status().isNoContent());
    }


    @Test
    void testAsignarTransportista() throws Exception {
        when(entregaService.asignarTransportista(eq(1L), eq("Juan Perez"), eq("Camioneta"), eq("ABC123"))).thenReturn(entrega);

        mockMvc.perform(put("/api/v1/entregas/1/asignar-transportista")
                .param("transportista", "Juan Perez")
                .param("vehiculo", "Camioneta")
                .param("patente", "ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transportista").value("Juan Perez"));
    }


    @Test
    void testGetCiudadesDisponibles() throws Exception {
        when(entregaService.getCiudadesDisponibles()).thenReturn(Arrays.asList("Santiago", "Valparaíso"));

        mockMvc.perform(get("/api/v1/entregas/ciudades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Santiago"))
                .andExpect(jsonPath("$[1]").value("Valparaíso"));
    }

    @Test
    void testGetTransportistasDisponibles() throws Exception {
        when(entregaService.getTransportistasDisponibles()).thenReturn(Arrays.asList("Juan Perez", "Carlos Gomez"));

        mockMvc.perform(get("/api/v1/entregas/transportistas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Juan Perez"))
                .andExpect(jsonPath("$[1]").value("Carlos Gomez"));
    }
}