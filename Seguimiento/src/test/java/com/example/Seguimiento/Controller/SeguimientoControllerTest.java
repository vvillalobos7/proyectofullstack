package com.example.Seguimiento.Controller;

import com.example.Seguimiento.Model.Seguimiento;
import com.example.Seguimiento.Service.SeguimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SeguimientoController.class)
class SeguimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeguimientoService seguimientoService;

    private Seguimiento seguimiento;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        seguimiento = new Seguimiento(1L, "En camino", "ACTIVO", LocalDateTime.now());
    }

    @Test
    void testListar() throws Exception {
        when(seguimientoService.obtenerTodos()).thenReturn(List.of(seguimiento));
        mockMvc.perform(get("/api/v1/seguimiento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descripcion").value("En camino"));
    }

    @Test
    void testObtenerPorId() throws Exception {
        when(seguimientoService.obtenerPorId(1L)).thenReturn(seguimiento);

        mockMvc.perform(get("/api/v1/seguimiento/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("En camino"));
    }

    @Test
    void testEliminar() throws Exception {
        doNothing().when(seguimientoService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/seguimiento/1"))
                .andExpect(status().isNoContent());

        verify(seguimientoService).eliminar(1L);
    }
}