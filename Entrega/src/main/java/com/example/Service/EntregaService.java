package com.example.Service;

import com.example.Model.EntregaModel;
import com.example.Repository.EntregaRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class EntregaService {

    private final EntregaRepository entregaRepository;

    public EntregaService(EntregaRepository entregaRepository) {
        this.entregaRepository = entregaRepository;
    }

    public List<EntregaModel> findAll() {
        return entregaRepository.findAll();
    }

    public EntregaModel findById(Long id) {
        return entregaRepository.findById(id)
              .orElseThrow(() -> new EntityNotFoundException("Entrega no encontrada con ID: " + id));
    }

    public List<EntregaModel> findByEstado(String estado) {
        return entregaRepository.findByEstado(estado);
    }

    public List<EntregaModel> findByTipoEntrega(String tipoEntrega) {
        return entregaRepository.findByTipoEntrega(tipoEntrega);
    }

    public List<EntregaModel> findByTransportista(String transportista) {
        return entregaRepository.findByTransportista(transportista);
    }

    public List<EntregaModel> findByCiudad(String ciudad) {
        return entregaRepository.findByCiudad(ciudad);
    }

    public List<EntregaModel> findByRegion(String region) {
        return entregaRepository.findByRegion(region);
    }

    public List<EntregaModel> findByRangoFechasProgramadas(LocalDateTime inicio, LocalDateTime fin) {
        return entregaRepository.findByFechaProgramadaBetween(inicio, fin);
    }

    public List<EntregaModel> findByRangoFechasRealizadas(LocalDateTime inicio, LocalDateTime fin) {
        return entregaRepository.findByFechaRealizadaBetween(inicio, fin);
    }

    public List<EntregaModel> findEntregasActivasTransportista(String transportista) {
        List<String> estadosActivos = Arrays.asList("PROGRAMADA", "EN_TRANSITO");
        return entregaRepository.findByTransportistaAndEstadoIn(transportista, estadosActivos);
    }

    public List<EntregaModel> findEntregasVencidas() {
        return entregaRepository.findEntregasVencidas(LocalDateTime.now());
    }

    public List<String> getCiudadesDisponibles() {
        return entregaRepository.findDistinctCiudades();
    }

    public List<String> getTransportistasDisponibles() {
        return entregaRepository.findDistinctTransportistas();
    }

    @Transactional
    public EntregaModel save(EntregaModel entrega) {
        return entregaRepository.save(entrega);
    }

    @Transactional
    public EntregaModel update(Long id, EntregaModel entregaDetails) {
        EntregaModel entrega = findById(id);

        entrega.setDireccionEntrega(entregaDetails.getDireccionEntrega());
        entrega.setCiudad(entregaDetails.getCiudad());
        entrega.setRegion(entregaDetails.getRegion());
        entrega.setTelefono(entregaDetails.getTelefono());
        entrega.setPersonaContacto(entregaDetails.getPersonaContacto());
        entrega.setFechaProgramada(entregaDetails.getFechaProgramada());
        entrega.setTransportista(entregaDetails.getTransportista());
        entrega.setVehiculo(entregaDetails.getVehiculo());
        entrega.setPatente(entregaDetails.getPatente());
        entrega.setObservaciones(entregaDetails.getObservaciones());
        entrega.setCosto(entregaDetails.getCosto());

        return entregaRepository.save(entrega);
    }

    @Transactional
    public EntregaModel asignarTransportista(Long id, String transportista, String vehiculo, String patente) {
        EntregaModel entrega = findById(id);
        entrega.setTransportista(transportista);
        entrega.setVehiculo(vehiculo);
        entrega.setPatente(patente);
        return entregaRepository.save(entrega);
    }

    @Transactional
    public EntregaModel cambiarEstado(Long id, String nuevoEstado) {
        EntregaModel entrega = findById(id);
        entrega.setEstado(nuevoEstado);

        if ("ENTREGADA".equals(nuevoEstado) || "DEVUELTA".equals(nuevoEstado)) {
            entrega.setFechaRealizada(LocalDateTime.now());
        }

        return entregaRepository.save(entrega);
    }

    @Transactional
    public EntregaModel iniciarTransporte(Long id) {
        return cambiarEstado(id, "EN_TRANSITO");
    }

    @Transactional
    public EntregaModel completarEntrega(Long id, Integer kmRecorridos) {
        EntregaModel entrega = findById(id);
        entrega.setEstado("ENTREGADA");
        entrega.setFechaRealizada(LocalDateTime.now());
        if (kmRecorridos != null) {
            entrega.setKmRecorridos(kmRecorridos);
        }
        return entregaRepository.save(entrega);
    }

    @Transactional
    public EntregaModel cancelarEntrega(Long id, String motivoCancelacion) {
        EntregaModel entrega = findById(id);
        entrega.setEstado("CANCELADA");
        entrega.setMotivoCancelacion(motivoCancelacion);
        return entregaRepository.save(entrega);
    }

    @Transactional
    public EntregaModel registrarDevolucion(Long id) {
        return cambiarEstado(id, "DEVUELTA");
    }

    @Transactional
    public void deleteById(Long id) {
        if (!entregaRepository.existsById(id)) {
            throw new RuntimeException("Entrega no encontrada con ID: " + id);
        }
        entregaRepository.deleteById(id);
    }

    public Long contarPorEstado(String estado) {
        return entregaRepository.countByEstado(estado);
    }

    public List<EntregaModel> findEntregasPorCiudadYPeriodo(String ciudad, LocalDateTime inicio, LocalDateTime fin) {
        return entregaRepository.findByCiudadAndFechaProgramadaBetween(ciudad, inicio, fin);
    }

    public Double getTotalCostoEntregasPorPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        Double total = entregaRepository.getTotalCostoEntregasByPeriodo(inicio, fin);
        return total != null ? total : 0.0;
    }

    public boolean validarDisponibilidadTransportista(String transportista, LocalDateTime fecha) {
        List<EntregaModel> entregasActivas = findEntregasActivasTransportista(transportista);
        return entregasActivas.stream()
                .noneMatch(e -> e.getFechaProgramada().toLocalDate().equals(fecha.toLocalDate()));
    }

    public List<EntregaModel> findEntregasHoy() {
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime todayEnd = todayStart.plusDays(1);
        return entregaRepository.findByFechaProgramadaBetween(todayStart, todayEnd);
    }
}