package com.example.Service;

import com.example.Model.IncidenciaModel;
import com.example.Model.TecnicoModel;
import com.example.Repository.IncidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;

    @Autowired
    public IncidenciaService(IncidenciaRepository incidenciaRepository) {
        this.incidenciaRepository = incidenciaRepository;
    }

    public List<IncidenciaModel> findAll() {
        return incidenciaRepository.findAll();
    }

    public Optional<IncidenciaModel> findById(Long id) {
        return incidenciaRepository.findById(id);
    }
    
    public List<IncidenciaModel> findByCliente(Long idUsuario) {
        return incidenciaRepository.findByUsuarioId(idUsuario);
    }
    
    public List<IncidenciaModel> findByEquipo(Long idEquipo) {
        return incidenciaRepository.findByEquipoId(idEquipo);
    }
    
    public List<IncidenciaModel> findByTecnico(Long idTecnico) {
        return incidenciaRepository.findByTecnicoId(idTecnico);
    }
    
    public List<IncidenciaModel> findByFechaReporte(Date inicio, Date fin) {
        return incidenciaRepository.findByFechaReporteBetween(inicio, fin);
    }

    public List<IncidenciaModel> findByEstado(String estado) {
        return incidenciaRepository.findByEstado(estado);
    }

    public List<IncidenciaModel> findByPrioridad(String prioridad) {
        return incidenciaRepository.findByPrioridad(prioridad);
    }

    public List<IncidenciaModel> findIncidenciasPendientes() {
        return incidenciaRepository.findByEstado("ABIERTA");
    }

    public Long countIncidenciasByUsuario(Long idUsuario) {
        return incidenciaRepository.countIncidenciasByUsuario(idUsuario);
    }

    @Transactional
    public IncidenciaModel save(IncidenciaModel incidencia) {
        if (incidencia.getFechaReporte() == null) {
            incidencia.setFechaReporte(new Date());
        }
        if (incidencia.getEstado() == null || incidencia.getEstado().isEmpty()) {
            incidencia.setEstado("ABIERTA");
        }
        if (incidencia.getPrioridad() == null || incidencia.getPrioridad().isEmpty()) {
            incidencia.setPrioridad("MEDIA");
        }
        return incidenciaRepository.save(incidencia);
    }

    @Transactional
    public IncidenciaModel update(Long id, IncidenciaModel incidenciaDetails) {
        Optional<IncidenciaModel> optionalIncidencia = findById(id);
        if (optionalIncidencia.isPresent()) {
            IncidenciaModel incidencia = optionalIncidencia.get();
            
            if (incidenciaDetails.getDescripcion() != null) {
                incidencia.setDescripcion(incidenciaDetails.getDescripcion());
            }
            
            if (incidenciaDetails.getEstado() != null) {
                incidencia.setEstado(incidenciaDetails.getEstado());
            }
            
            if (incidenciaDetails.getPrioridad() != null) {
                incidencia.setPrioridad(incidenciaDetails.getPrioridad());
            }
            
            if (incidenciaDetails.getTecnico() != null) {
                incidencia.setTecnico(incidenciaDetails.getTecnico());
            }
            
            if (incidenciaDetails.getEquipo() != null) {
                incidencia.setEquipo(incidenciaDetails.getEquipo());
            }
            
            if (incidenciaDetails.getSolucion() != null) {
                incidencia.setSolucion(incidenciaDetails.getSolucion());
            }
            
            if (incidenciaDetails.getFechaResolucion() != null) {
                incidencia.setFechaResolucion(incidenciaDetails.getFechaResolucion());
            }
            
            return incidenciaRepository.save(incidencia);
        }
        throw new RuntimeException("Incidencia no encontrada con ID: " + id);
    }
    
    @Transactional
    public IncidenciaModel asignarTecnico(Long idIncidencia, TecnicoModel tecnico) {
        Optional<IncidenciaModel> optionalIncidencia = findById(idIncidencia);
        if (optionalIncidencia.isPresent()) {
            IncidenciaModel incidencia = optionalIncidencia.get();
            incidencia.setTecnico(tecnico);
            
            if ("ABIERTA".equals(incidencia.getEstado())) {
                incidencia.setEstado("EN_PROCESO");
            }
            
            return incidenciaRepository.save(incidencia);
        }
        throw new RuntimeException("Incidencia no encontrada con ID: " + idIncidencia);
    }

    @Transactional
    public IncidenciaModel cerrarIncidencia(Long idIncidencia, String solucion) {
        Optional<IncidenciaModel> optionalIncidencia = findById(idIncidencia);
        if (optionalIncidencia.isPresent()) {
            IncidenciaModel incidencia = optionalIncidencia.get();
            incidencia.setEstado("CERRADA");
            incidencia.setSolucion(solucion);
            incidencia.setFechaResolucion(new Date());
            
            return incidenciaRepository.save(incidencia);
        }
        throw new RuntimeException("Incidencia no encontrada con ID: " + idIncidencia);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!incidenciaRepository.existsById(id)) {
            throw new RuntimeException("Incidencia no encontrada con ID: " + id);
        }
        incidenciaRepository.deleteById(id);
    }
}