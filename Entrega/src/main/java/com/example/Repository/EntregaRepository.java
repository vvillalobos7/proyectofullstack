package com.example.Repository;

import com.example.Model.EntregaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<EntregaModel, Long> {

    List<EntregaModel> findByEstado(String estado);

    List<EntregaModel> findByTipoEntrega(String tipoEntrega);

    List<EntregaModel> findByTransportista(String transportista);

    List<EntregaModel> findByCiudad(String ciudad);

    List<EntregaModel> findByRegion(String region);

    @Query("SELECT e FROM EntregaModel e WHERE e.fechaProgramada BETWEEN :inicio AND :fin")
    List<EntregaModel> findByFechaProgramadaBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT e FROM EntregaModel e WHERE e.fechaRealizada BETWEEN :inicio AND :fin")
    List<EntregaModel> findByFechaRealizadaBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT e FROM EntregaModel e WHERE e.transportista = :transportista AND e.estado IN :estados")
    List<EntregaModel> findByTransportistaAndEstadoIn(@Param("transportista") String transportista, @Param("estados") List<String> estados);

    @Query("SELECT e FROM EntregaModel e WHERE e.estado = 'PROGRAMADA' AND e.fechaProgramada < :fecha")
    List<EntregaModel> findEntregasVencidas(@Param("fecha") LocalDateTime fecha);

    @Query("SELECT DISTINCT e.ciudad FROM EntregaModel e ORDER BY e.ciudad")
    List<String> findDistinctCiudades();

    @Query("SELECT DISTINCT e.transportista FROM EntregaModel e WHERE e.transportista IS NOT NULL ORDER BY e.transportista")
    List<String> findDistinctTransportistas();

    @Query("SELECT COUNT(e) FROM EntregaModel e WHERE e.estado = :estado")
    Long countByEstado(@Param("estado") String estado);

    @Query("SELECT e FROM EntregaModel e WHERE e.ciudad = :ciudad AND e.fechaProgramada BETWEEN :inicio AND :fin")
    List<EntregaModel> findByCiudadAndFechaProgramadaBetween(@Param("ciudad") String ciudad, @Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT SUM(e.costo) FROM EntregaModel e WHERE e.estado = 'ENTREGADA' AND e.fechaRealizada BETWEEN :inicio AND :fin")
    Double getTotalCostoEntregasByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}