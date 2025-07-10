package com.example.Repository;

import com.example.Model.MantenimientoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<MantenimientoModel, Long> {

    List<MantenimientoModel> findByTipo(String tipo);

    List<MantenimientoModel> findByFechaProgramadaBetween(LocalDateTime inicio, LocalDateTime fin);
}