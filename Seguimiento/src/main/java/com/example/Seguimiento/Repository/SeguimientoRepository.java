package com.example.Seguimiento.Repository;

import com.example.Seguimiento.Model.Seguimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {
}