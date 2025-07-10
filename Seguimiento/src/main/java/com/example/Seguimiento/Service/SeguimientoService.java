package com.example.Seguimiento.Service;

import com.example.Seguimiento.Model.Seguimiento;
import com.example.Seguimiento.Repository.SeguimientoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeguimientoService {

    private final SeguimientoRepository seguimientoRepository;

    public SeguimientoService(SeguimientoRepository seguimientoRepository) {
        this.seguimientoRepository = seguimientoRepository;
    }

    public List<Seguimiento> obtenerTodos() {
        return seguimientoRepository.findAll();
    }

    public Seguimiento guardar(Seguimiento seguimiento) {
        return seguimientoRepository.save(seguimiento);
    }

    public Seguimiento obtenerPorId(Long id) {
        return seguimientoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seguimiento no encontrado con ID: " + id));
    }

    public void eliminar(Long id) {
        if (!seguimientoRepository.existsById(id)) {
            throw new EntityNotFoundException("Seguimiento no encontrado con ID: " + id);
        }
        seguimientoRepository.deleteById(id);
    }
}