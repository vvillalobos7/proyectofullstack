package com.example.Repository;

import com.example.Model.InventarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<InventarioModel, Long> {

    Optional<InventarioModel> findByEquipoIdEquipo(Long idEquipo);
    
    List<InventarioModel> findByEstado(String estado);
    List<InventarioModel> findByUbicacion(String ubicacion);
    
    @Query("SELECT i FROM InventarioModel i WHERE i.stockDisponible <= i.stockMinimo")
    List<InventarioModel> findStockCritico();
    
    @Query("SELECT i FROM InventarioModel i WHERE i.stockDisponible = 0")
    List<InventarioModel> findStockAgotado();
    
   
    @Query("SELECT i FROM InventarioModel i WHERE i.equipo.tipoEquipo = :tipoEquipo")
    List<InventarioModel> findByTipoEquipo(@Param("tipoEquipo") String tipoEquipo);
    
    @Query("SELECT i FROM InventarioModel i WHERE i.stockDisponible >= :cantidad")
    List<InventarioModel> findByStockDisponibleGreaterThanEqual(@Param("cantidad") Integer cantidad);
    
    @Query("SELECT COALESCE(SUM(i.stockTotal), 0) FROM InventarioModel i")
    Long getTotalStock();
    
    @Query("SELECT COALESCE(SUM(i.stockDisponible), 0) FROM InventarioModel i")
    Long getTotalStockDisponible();
    
    @Query("SELECT COALESCE(SUM(i.stockArrendado), 0) FROM InventarioModel i")
    Long getTotalStockArrendado();
}