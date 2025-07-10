package com.example.Service;

import com.example.Model.InventarioModel;
import com.example.Repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    @Autowired
    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    public List<InventarioModel> findAll() {
        return inventarioRepository.findAll();
    }

    public InventarioModel findById(Long id) {
        return inventarioRepository.findById(id)
              .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + id));
    }
    
    public Optional<InventarioModel> findByEquipo(Long idEquipo) {
        return inventarioRepository.findByEquipoIdEquipo(idEquipo);
    }
    
    public List<InventarioModel> findByEstado(String estado) {
        return inventarioRepository.findByEstado(estado);
    }
    
    public List<InventarioModel> findByUbicacion(String ubicacion) {
        return inventarioRepository.findByUbicacion(ubicacion);
    }
    
    public List<InventarioModel> findStockCritico() {
        return inventarioRepository.findStockCritico();
    }
    
    public List<InventarioModel> findStockAgotado() {
        return inventarioRepository.findStockAgotado();
    }
    
    public List<InventarioModel> findByTipoEquipo(String tipoEquipo) {
        return inventarioRepository.findByTipoEquipo(tipoEquipo);
    }
    
    public List<InventarioModel> findDisponiblesPorCantidad(Integer cantidad) {
        return inventarioRepository.findByStockDisponibleGreaterThanEqual(cantidad);
    }

    @Transactional
    public InventarioModel save(InventarioModel inventario) {
        return inventarioRepository.save(inventario);
    }

    @Transactional
    public InventarioModel update(Long id, InventarioModel inventarioDetails) {
        InventarioModel inventario = findById(id);
        
        inventario.setStockDisponible(inventarioDetails.getStockDisponible());
        inventario.setStockMinimo(inventarioDetails.getStockMinimo());
        inventario.setStockTotal(inventarioDetails.getStockTotal());
        inventario.setStockArrendado(inventarioDetails.getStockArrendado());
        inventario.setUbicacion(inventarioDetails.getUbicacion());
        inventario.setObservaciones(inventarioDetails.getObservaciones());
        
        return inventarioRepository.save(inventario);
    }
    
    @Transactional
    public InventarioModel actualizarStock(Long id, Integer stockDisponible, Integer stockArrendado) {
        InventarioModel inventario = findById(id);
        inventario.setStockDisponible(stockDisponible);
        inventario.setStockArrendado(stockArrendado);
        return inventarioRepository.save(inventario);
    }
    
    @Transactional
    public InventarioModel aumentarStock(Long id, Integer cantidad) {
        InventarioModel inventario = findById(id);
        inventario.setStockDisponible(inventario.getStockDisponible() + cantidad);
        inventario.setStockTotal(inventario.getStockTotal() + cantidad);
        return inventarioRepository.save(inventario);
    }
    
    @Transactional
    public InventarioModel reducirStock(Long id, Integer cantidad) {
        InventarioModel inventario = findById(id);
        if (inventario.getStockDisponible() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + inventario.getStockDisponible());
        }
        inventario.setStockDisponible(inventario.getStockDisponible() - cantidad);
        inventario.setStockArrendado(inventario.getStockArrendado() + cantidad);
        return inventarioRepository.save(inventario);
    }
    
    @Transactional
    public InventarioModel devolverEquipo(Long id, Integer cantidad) {
        InventarioModel inventario = findById(id);
        inventario.setStockDisponible(inventario.getStockDisponible() + cantidad);
        inventario.setStockArrendado(inventario.getStockArrendado() - cantidad);
        return inventarioRepository.save(inventario);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!inventarioRepository.existsById(id)) {
            throw new RuntimeException("Inventario no encontrado con ID: " + id);
        }
        inventarioRepository.deleteById(id);
    }
    
    // Métodos para reportes
    public Long getTotalStock() {
        return inventarioRepository.getTotalStock();
    }
    
    public Long getTotalStockDisponible() {
        return inventarioRepository.getTotalStockDisponible();
    }
    
    public Long getTotalStockArrendado() {
        return inventarioRepository.getTotalStockArrendado();
    }
    
    public boolean verificarDisponibilidad(Long idEquipo, Integer cantidadRequerida) {
        Optional<InventarioModel> inventario = findByEquipo(idEquipo);
        return inventario.isPresent() && inventario.get().getStockDisponible() >= cantidadRequerida;
    }
}