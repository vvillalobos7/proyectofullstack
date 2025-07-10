package com.example.Service;

import com.example.Model.InventarioModel;
import com.example.Model.EquipoModel;
import com.example.Repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

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
    void testFindAll() {
      
        List<InventarioModel> inventarios = Arrays.asList(inventarioTest);
        when(inventarioRepository.findAll()).thenReturn(inventarios);

      
        List<InventarioModel> result = inventarioService.findAll();

      
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(inventarioTest, result.get(0));
        verify(inventarioRepository).findAll();
    }

    @Test
    void testFindById_Success() {
        
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventarioTest));

       
        InventarioModel result = inventarioService.findById(1L);

        
        assertNotNull(result);
        assertEquals(inventarioTest, result);
        verify(inventarioRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
       
        when(inventarioRepository.findById(999L)).thenReturn(Optional.empty());

      
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> inventarioService.findById(999L));
        assertEquals("Inventario no encontrado con ID: 999", exception.getMessage());
        verify(inventarioRepository).findById(999L);
    }

    @Test
    void testFindByEquipo() {
        
        when(inventarioRepository.findByEquipoIdEquipo(1L)).thenReturn(Optional.of(inventarioTest));

        
        Optional<InventarioModel> result = inventarioService.findByEquipo(1L);

        
        assertTrue(result.isPresent());
        assertEquals(inventarioTest, result.get());
        verify(inventarioRepository).findByEquipoIdEquipo(1L);
    }

    @Test
    void testFindByEstado() {
       
        List<InventarioModel> inventarios = Arrays.asList(inventarioTest);
        when(inventarioRepository.findByEstado("DISPONIBLE")).thenReturn(inventarios);

       
        List<InventarioModel> result = inventarioService.findByEstado("DISPONIBLE");

        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(inventarioTest, result.get(0));
        verify(inventarioRepository).findByEstado("DISPONIBLE");
    }

    @Test
    void testFindByUbicacion() {
       
        List<InventarioModel> inventarios = Arrays.asList(inventarioTest);
        when(inventarioRepository.findByUbicacion("Bodega A")).thenReturn(inventarios);

       
        List<InventarioModel> result = inventarioService.findByUbicacion("Bodega A");

       
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(inventarioTest, result.get(0));
        verify(inventarioRepository).findByUbicacion("Bodega A");
    }

    @Test
    void testFindStockCritico() {
        
        List<InventarioModel> inventarios = Arrays.asList(inventarioTest);
        when(inventarioRepository.findStockCritico()).thenReturn(inventarios);

       
        List<InventarioModel> result = inventarioService.findStockCritico();

       
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(inventarioRepository).findStockCritico();
    }

    @Test
    void testFindStockAgotado() {
    
        List<InventarioModel> inventarios = Arrays.asList(inventarioTest);
        when(inventarioRepository.findStockAgotado()).thenReturn(inventarios);

      
        List<InventarioModel> result = inventarioService.findStockAgotado();

       
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(inventarioRepository).findStockAgotado();
    }

    @Test
    void testSave() {
       
        when(inventarioRepository.save(any(InventarioModel.class))).thenReturn(inventarioTest);

       
        InventarioModel result = inventarioService.save(inventarioTest);

     
        assertNotNull(result);
        assertEquals(inventarioTest, result);
        verify(inventarioRepository).save(inventarioTest);
    }

    @Test
    void testUpdate_Success() {
       
        InventarioModel updatedData = new InventarioModel();
        updatedData.setStockDisponible(20);
        updatedData.setStockMinimo(8);
        updatedData.setStockTotal(25);
        updatedData.setStockArrendado(5);
        updatedData.setUbicacion("Bodega B");
        updatedData.setObservaciones("Updated item");

        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventarioTest));
        when(inventarioRepository.save(any(InventarioModel.class))).thenReturn(inventarioTest);

      
        InventarioModel result = inventarioService.update(1L, updatedData);

       
        assertNotNull(result);
        verify(inventarioRepository).findById(1L);
        verify(inventarioRepository).save(any(InventarioModel.class));
    }

    @Test
    void testAumentarStock() {
     
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventarioTest));
        when(inventarioRepository.save(any(InventarioModel.class))).thenReturn(inventarioTest);

       
        InventarioModel result = inventarioService.aumentarStock(1L, 5);

      
        assertNotNull(result);
        verify(inventarioRepository).findById(1L);
        verify(inventarioRepository).save(any(InventarioModel.class));
    }

    @Test
    void testReducirStock_Success() {
      
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventarioTest));
        when(inventarioRepository.save(any(InventarioModel.class))).thenReturn(inventarioTest);

    
        InventarioModel result = inventarioService.reducirStock(1L, 5);

        
        assertNotNull(result);
        verify(inventarioRepository).findById(1L);
        verify(inventarioRepository).save(any(InventarioModel.class));
    }

    @Test
    void testReducirStock_StockInsuficiente() {
       
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventarioTest));

      
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> inventarioService.reducirStock(1L, 15));
        assertTrue(exception.getMessage().contains("Stock insuficiente"));
        verify(inventarioRepository).findById(1L);
        verify(inventarioRepository, never()).save(any(InventarioModel.class));
    }

    @Test
    void testDevolverEquipo() {
      
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventarioTest));
        when(inventarioRepository.save(any(InventarioModel.class))).thenReturn(inventarioTest);

       
        InventarioModel result = inventarioService.devolverEquipo(1L, 3);

        
        assertNotNull(result);
        verify(inventarioRepository).findById(1L);
        verify(inventarioRepository).save(any(InventarioModel.class));
    }

    @Test
    void testDeleteById_Success() {
    
        when(inventarioRepository.existsById(1L)).thenReturn(true);

       
        inventarioService.deleteById(1L);

       
        verify(inventarioRepository).existsById(1L);
        verify(inventarioRepository).deleteById(1L);
    }

    @Test
    void testDeleteById_NotFound() {
       
        when(inventarioRepository.existsById(999L)).thenReturn(false);

       
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> inventarioService.deleteById(999L));
        assertEquals("Inventario no encontrado con ID: 999", exception.getMessage());
        verify(inventarioRepository).existsById(999L);
        verify(inventarioRepository, never()).deleteById(999L);
    }

    @Test
    void testGetTotalStock() {
       
        when(inventarioRepository.getTotalStock()).thenReturn(100L);

    
        Long result = inventarioService.getTotalStock();

        
        assertEquals(100L, result);
        verify(inventarioRepository).getTotalStock();
    }

    @Test
    void testGetTotalStockDisponible() {
        
        when(inventarioRepository.getTotalStockDisponible()).thenReturn(80L);

       
        Long result = inventarioService.getTotalStockDisponible();

       
        assertEquals(80L, result);
        verify(inventarioRepository).getTotalStockDisponible();
    }

    @Test
    void testGetTotalStockArrendado() {
        
        when(inventarioRepository.getTotalStockArrendado()).thenReturn(20L);

        
        Long result = inventarioService.getTotalStockArrendado();

        
        assertEquals(20L, result);
        verify(inventarioRepository).getTotalStockArrendado();
    }

    @Test
    void testVerificarDisponibilidad_True() {
        
        when(inventarioRepository.findByEquipoIdEquipo(1L)).thenReturn(Optional.of(inventarioTest));

        
        boolean result = inventarioService.verificarDisponibilidad(1L, 5);

        
        assertTrue(result);
        verify(inventarioRepository).findByEquipoIdEquipo(1L);
    }

    @Test
    void testVerificarDisponibilidad_False_StockInsuficiente() {
        
        when(inventarioRepository.findByEquipoIdEquipo(1L)).thenReturn(Optional.of(inventarioTest));

        // When
        boolean result = inventarioService.verificarDisponibilidad(1L, 15);

    
        assertFalse(result);
        verify(inventarioRepository).findByEquipoIdEquipo(1L);
    }

    @Test
    void testVerificarDisponibilidad_False_InventarioNoEncontrado() {

        when(inventarioRepository.findByEquipoIdEquipo(999L)).thenReturn(Optional.empty());

        boolean result = inventarioService.verificarDisponibilidad(999L, 5);

        assertFalse(result);
        verify(inventarioRepository).findByEquipoIdEquipo(999L);
    }
}