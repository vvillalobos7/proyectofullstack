package com.example.Controller;

import com.example.Model.MantenimientoModel;
import com.example.Service.MantenimientoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(MantenimientoController.class)
class MantenimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MantenimientoService mantenimientoService;

    private MantenimientoModel mantenimiento;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mantenimiento = new MantenimientoModel();
        mantenimiento.setId(1L);
        mantenimiento.setTipo("Correctivo");
        mantenimiento.setFechaProgramada(LocalDateTime.of(2025, 7, 10, 10, 0));
        mantenimiento.setFechaRealizada(null);
    }

    @Test
    void testGetAllMantenimientos() throws Exception {
        when(mantenimientoService.findAll()).thenReturn(List.of(mantenimiento));

        mockMvc.perform(get("/api/v1/mantenimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo", is("Correctivo")));
    }

    @Test
    void testGetMantenimientoById() throws Exception {
        when(mantenimientoService.findById(1L)).thenReturn(mantenimiento);

        mockMvc.perform(get("/api/v1/mantenimientos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", is("Correctivo")));
    }

    @Test
    void testGetMantenimientoByIdNotFound() throws Exception {
        when(mantenimientoService.findById(1L)).thenThrow(new RuntimeException("No encontrado"));

        mockMvc.perform(get("/api/v1/mantenimientos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateMantenimiento() throws Exception {
        MantenimientoModel nuevo = new MantenimientoModel();
        nuevo.setFechaProgramada(LocalDateTime.of(2025, 7, 10, 10, 0));
        nuevo.setTipo("Correctivo");

        when(mantenimientoService.save(eq(nuevo))).thenReturn(mantenimiento);

        mockMvc.perform(post("/api/v1/mantenimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo", is("Correctivo")));
    }

    @Test
    void testDeleteMantenimientoSuccess() throws Exception {
        doNothing().when(mantenimientoService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/mantenimientos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteMantenimientoNotFound() throws Exception {
        doThrow(new RuntimeException("No encontrado")).when(mantenimientoService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/mantenimientos/1"))
                .andExpect(status().isNotFound());
    }
}