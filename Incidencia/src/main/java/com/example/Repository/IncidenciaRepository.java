package com.example.Repository;

import com.example.Model.IncidenciaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncidenciaRepository extends JpaRepository<IncidenciaModel, Long> {
    
  
    @Query("SELECT COUNT(i) FROM IncidenciaModel i WHERE i.usuario.idusuario = :idUsuario")
    Long countIncidenciasByUsuario(@Param("idUsuario") Long idUsuario);
    
   
    @Query("SELECT i FROM IncidenciaModel i WHERE i.usuario.idusuario = :idUsuario")
    List<IncidenciaModel> findByUsuarioId(@Param("idUsuario") Long idUsuario);
    
    @Query("SELECT i FROM IncidenciaModel i WHERE i.estado = :estado")
    List<IncidenciaModel> findByEstado(@Param("estado") String estado);
    
    @Query("SELECT i FROM IncidenciaModel i WHERE i.prioridad = :prioridad")
    List<IncidenciaModel> findByPrioridad(@Param("prioridad") String prioridad);
    
    @Query("SELECT i FROM IncidenciaModel i WHERE i.usuario.idusuario = :idUsuario AND i.estado = :estado")
    List<IncidenciaModel> findByUsuarioIdAndEstado(@Param("idUsuario") Long idUsuario, 
                                                   @Param("estado") String estado);
    
    
    @Query("SELECT i FROM IncidenciaModel i ORDER BY i.fechaReporte DESC")
    List<IncidenciaModel> findAllOrderByFechaCreacionDesc();

    @Query("SELECT i FROM IncidenciaModel i WHERE i.usuario.idusuario = :idUsuario ORDER BY i.fechaReporte DESC")
    List<IncidenciaModel> findByUsuarioIdOrderByFechaCreacionDesc(@Param("idUsuario") Long idUsuario);
    
    @Query("SELECT i FROM IncidenciaModel i WHERE i.tecnico.idTecnico = :idTecnico")
    List<IncidenciaModel> findByTecnicoId(@Param("idTecnico") Long idTecnico);
    
    @Query("SELECT i FROM IncidenciaModel i WHERE i.tecnico IS NULL")
    List<IncidenciaModel> findIncidenciasSinAsignar();
    
    @Query("SELECT i FROM IncidenciaModel i WHERE i.equipo.id = :idEquipo")
    List<IncidenciaModel> findByEquipoId(@Param("idEquipo") Long idEquipo);
    
    
    @Query("SELECT i FROM IncidenciaModel i WHERE i.fechaReporte BETWEEN :fechaInicio AND :fechaFin")
    List<IncidenciaModel> findByFechaReporteBetween(@Param("fechaInicio") Date fechaInicio, 
                                                   @Param("fechaFin") Date fechaFin);
    
    @Query("SELECT i.estado, COUNT(i) FROM IncidenciaModel i GROUP BY i.estado")
    List<Object[]> countByEstado();
    
    @Query("SELECT i.prioridad, COUNT(i) FROM IncidenciaModel i GROUP BY i.prioridad")
    List<Object[]> countByPrioridad();
    
    @Query("SELECT i FROM IncidenciaModel i WHERE i.estado = 'ABIERTA' ORDER BY i.fechaReporte ASC")
    List<IncidenciaModel> findIncidenciasAbiertasAntiguasFirst();
    
    @Query("SELECT i FROM IncidenciaModel i WHERE i.estado IN :estados")
    List<IncidenciaModel> findByEstadoIn(@Param("estados") List<String> estados);
}