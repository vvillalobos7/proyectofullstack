package com.example.Controller;

import com.example.Model.InventarioModel;
import com.example.Model.EquipoModel;
import com.example.Service.InventarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventarioController.class)
class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioService inventarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private InventarioModel inventarioTest;
    private EquipoModel equipoTest;

    @BeforeEach
    void setUp() {
        equipoTest = new EquipoModel();
        
        inventarioTest = new InventarioModel();
        inventarioTest.setIdInventario(1L);
        inventarioTest.setEquipo(equipoTest);
        inventarioTest.setStockDisponible(10);
        inventarioTest.setStockMinimo(5);
        inventarioTest.setStockTotal(15);
        inventarioTest.setStockArrendado(5);
        inventarioTest.setUbicacion("Bodega A");
        inventarioTest.setEstado("DISPONIBLE");
        inventarioTest.setObservaciones("Test item");
    }

    @Test
    void testGetAllInventarios() throws Exception {
        
        List<InventarioModel> inventarios = Arrays.asList(inventarioTest);
        when(inventarioService.findAll()).thenReturn(inventarios);

        
        mockMvc.perform(get("/api/v1/inventario"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idInventario").value(1))
                .andExpect(jsonPath("$[0].stockDisponible").value(10))
                .andExpect(jsonPath("$[0].ubicacion").value("Bodega A"));

        verify(inventarioService).findAll();
    }

    @Test
    void testGetInventarioById_Success() throws Exception {
        
        when(inventarioService.findById(1L)).thenReturn(inventarioTest);

       
        mockMvc.perform(get("/api/v1/inventario/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idInventario").value(1))
                .andExpect(jsonPath("$.stockDisponible").value(10))
                .andExpect(jsonPath("$.ubicacion").value("Bodega A"));

        verify(inventarioService).findById(1L);
    }

    @Test
    void testGetInventarioById_NotFound() throws Exception {
       
        when(inventarioService.findById(999L)).thenThrow(new RuntimeException("Inventario no encontrado"));

        
        mockMvc.perform(get("/api/v1/inventario/999"))
                .andExpect(status().isNotFound());

        verify(inventarioService).findById(999L);
    }

    @Test
    void testGetInventarioByEquipo_Success() throws Exception {
      
        when(inventarioService.findByEquipo(1L)).thenReturn(Optional.of(inventarioTest));

        
        mockMvc.perform(get("/api/v1/inventario/equipo/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idInventario").value(1))
                .andExpect(jsonPath("$.stockDisponible").value(10));

        verify(inventarioService).findByEquipo(1L);
    }

    @Test
    void testGetInventarioByEquipo_NotFound() throws Exception {
       
        when(inventarioService.findByEquipo(999L)).thenReturn(Optional.empty());

        
        mockMvc.perform(get("/api/v1/inventario/equipo/999"))
                .andExpect(status().isNotFound());

        verify(inventarioService).findByEquipo(999L);
    }

    @Test
    void testGetInventariosByEstado() throws Exception {
        
        List<InventarioModel> inventarios = Arrays.asList(inventarioTest);
        when(inventarioService.findByEstado("DISPONIBLE")).thenReturn(inventarios);

      
        mockMvc.perform(get("/api/v1/inventario/estado/DISPONIBLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(inventarioService).findByEstado("DISPONIBLE");
    }

    @Test
    void testGetInventariosByUbicacion() throws Exception {
      
        List<InventarioModel> inventarios = Arrays.asList(inventarioTest);
        when(inventarioService.findByUbicacion("Bodega A")).thenReturn(inventarios);

       
        mockMvc.perform(get("/api/v1/inventario/ubicacion/Bodega A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(inventarioService).findByUbicacion("Bodega A");
    }

    @Test
    void testGetStockCritico() throws Exception {
        
        List<InventarioModel> inventarios = Arrays.asList(inventarioTest);
        when(inventarioService.findStockCritico()).thenReturn(inventarios);

        
        mockMvc.perform(get("/api/v1/inventario/stock-critico"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(inventarioService).findStockCritico();
    }

    @Test
    void testGetStockAgotado() throws Exception {
       
        List<InventarioModel> inventarios = Arrays.asList(inventarioTest);
        when(inventarioService.findStockAgotado()).thenReturn(inventarios);

       
        mockMvc.perform(get("/api/v1/inventario/stock-agotado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(inventarioService).findStockAgotado();
    }

    @Test
    void testCreateInventario() throws Exception {
        
        when(inventarioService.save(any(InventarioModel.class))).thenReturn(inventarioTest);

        
        mockMvc.perform(post("/api/v1/inventario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventarioTest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idInventario").value(1))
                .andExpect(jsonPath("$.stockDisponible").value(10));

        verify(inventarioService).save(any(InventarioModel.class));
    }

    @Test
    void testUpdateInventario_Success() throws Exception {
       
        when(inventarioService.update(eq(1L), any(InventarioModel.class))).thenReturn(inventarioTest);

       
        mockMvc.perform(put("/api/v1/inventario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventarioTest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idInventario").value(1));

        verify(inventarioService).update(eq(1L), any(InventarioModel.class));
    }

    @Test
    void testUpdateInventario_NotFound() throws Exception {
       
        when(inventarioService.update(eq(999L), any(InventarioModel.class)))
                .thenThrow(new RuntimeException("Inventario no encontrado"));

        
        mockMvc.perform(put("/api/v1/inventario/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventarioTest)))
                .andExpect(status().isNotFound());

        verify(inventarioService).update(eq(999L), any(InventarioModel.class));
    }

    @Test
    void testDeleteInventario_Success() throws Exception {
       
        doNothing().when(inventarioService).deleteById(1L);

       
        mockMvc.perform(delete("/api/v1/inventario/1"))
                .andExpect(status().isOk());

        verify(inventarioService).deleteById(1L);
    }

    @Test
    void testDeleteInventario_NotFound() throws Exception {
      
        doThrow(new RuntimeException("Inventario no encontrado"))
                .when(inventarioService).deleteById(999L);

       
        mockMvc.perform(delete("/api/v1/inventario/999"))
                .andExpect(status().isNotFound());

        verify(inventarioService).deleteById(999L);
    }

    @Test
    void testAumentarStock_Success() throws Exception {
       
        when(inventarioService.aumentarStock(1L, 5)).thenReturn(inventarioTest);

        
        mockMvc.perform(put("/api/v1/inventario/1/aumentar-stock?cantidad=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idInventario").value(1));

        verify(inventarioService).aumentarStock(1L, 5);
    }

    @Test
    void testAumentarStock_BadRequest() throws Exception {
        
        when(inventarioService.aumentarStock(1L, 5))
                .thenThrow(new RuntimeException("Error"));

        
        mockMvc.perform(put("/api/v1/inventario/1/aumentar-stock?cantidad=5"))
                .andExpect(status().isBadRequest());

        verify(inventarioService).aumentarStock(1L, 5);
    }

    @Test
    void testReducirStock_Success() throws Exception {
        
        when(inventarioService.reducirStock(1L, 3)).thenReturn(inventarioTest);

        
        mockMvc.perform(put("/api/v1/inventario/1/reducir-stock?cantidad=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idInventario").value(1));

        verify(inventarioService).reducirStock(1L, 3);
    }

    @Test
    void testReducirStock_BadRequest() throws Exception {
       
        when(inventarioService.reducirStock(1L, 15))
                .thenThrow(new RuntimeException("Stock insuficiente"));

        
        mockMvc.perform(put("/api/v1/inventario/1/reducir-stock?cantidad=15"))
                .andExpect(status().isBadRequest());

        verify(inventarioService).reducirStock(1L, 15);
    }

    @Test
    void testDevolverEquipo_Success() throws Exception {
       
        when(inventarioService.devolverEquipo(1L, 2)).thenReturn(inventarioTest);

        
        mockMvc.perform(put("/api/v1/inventario/1/devolver-equipo?cantidad=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idInventario").value(1));

        verify(inventarioService).devolverEquipo(1L, 2);
    }

    @Test
    void testVerificarDisponibilidad() throws Exception {
     
        when(inventarioService.verificarDisponibilidad(1L, 5)).thenReturn(true);

       
        mockMvc.perform(get("/api/v1/inventario/verificar-disponibilidad/1?cantidad=5"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(inventarioService).verificarDisponibilidad(1L, 5);
    }

    @Test
    void testGetTotalStock() throws Exception {
    
        when(inventarioService.getTotalStock()).thenReturn(100L);

       
        mockMvc.perform(get("/api/v1/inventario/reportes/total-stock"))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));

        verify(inventarioService).getTotalStock();
    }

    @Test
    void testGetTotalStockDisponible() throws Exception {
       
        when(inventarioService.getTotalStockDisponible()).thenReturn(80L);

        
        mockMvc.perform(get("/api/v1/inventario/reportes/stock-disponible"))
                .andExpect(status().isOk())
                .andExpect(content().string("80"));

        verify(inventarioService).getTotalStockDisponible();
    }

    @Test
    void testGetTotalStockArrendado() throws Exception {
        
        when(inventarioService.getTotalStockArrendado()).thenReturn(20L);

       
        mockMvc.perform(get("/api/v1/inventario/reportes/stock-arrendado"))
                .andExpect(status().isOk())
                .andExpect(content().string("20"));

        verify(inventarioService).getTotalStockArrendado();
    }

    @Test
    void testGetDisponiblesPorCantidad() throws Exception {
       
        List<InventarioModel> inventarios = Arrays.asList(inventarioTest);
        when(inventarioService.findDisponiblesPorCantidad(5)).thenReturn(inventarios);

       
        mockMvc.perform(get("/api/v1/inventario/disponibles/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(inventarioService).findDisponiblesPorCantidad(5);
    }

    @Test
    void testGetInventariosByTipoEquipo() throws Exception {
       
        List<InventarioModel> inventarios = Arrays.asList(inventarioTest);
        when(inventarioService.findByTipoEquipo("LAPTOP")).thenReturn(inventarios);

       
        mockMvc.perform(get("/api/v1/inventario/tipo-equipo/LAPTOP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(inventarioService).findByTipoEquipo("LAPTOP");
    }
}