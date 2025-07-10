package com.example.Service;

import com.example.Model.MantenimientoModel;
import com.example.Repository.MantenimientoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MantenimientoService {

    private final MantenimientoRepository mantenimientoRepository;

    public MantenimientoService(MantenimientoRepository mantenimientoRepository) {
        this.mantenimientoRepository = mantenimientoRepository;
    }

    public List<MantenimientoModel> findAll() {
        return mantenimientoRepository.findAll();
    }

    public MantenimientoModel findById(Long id) {
        return mantenimientoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mantenimiento no encontrado con ID: " + id));
    }

    @Transactional
    public MantenimientoModel save(MantenimientoModel mantenimiento) {
        return mantenimientoRepository.save(mantenimiento);
    }

    @Transactional
    public MantenimientoModel update(Long id, MantenimientoModel updatedData) {
        MantenimientoModel mantenimiento = findById(id);

        mantenimiento.setFechaProgramada(updatedData.getFechaProgramada());
        mantenimiento.setFechaRealizada(updatedData.getFechaRealizada());
        mantenimiento.setTipo(updatedData.getTipo());

        return mantenimientoRepository.save(mantenimiento);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!mantenimientoRepository.existsById(id)) {
            throw new EntityNotFoundException("Mantenimiento no encontrado con ID: " + id);
        }
        mantenimientoRepository.deleteById(id);
    }
}