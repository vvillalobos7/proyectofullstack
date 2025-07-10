package com.example.Controller;

import com.example.Model.MantenimientoModel;
import com.example.Service.MantenimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mantenimientos")
@Tag(name = "Mantenimientos", description = "Operaciones relacionadas con mantenimientos de maquinaria")
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;

    public MantenimientoController(MantenimientoService mantenimientoService) {
        this.mantenimientoService = mantenimientoService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los mantenimientos", description = "Devuelve una lista con todos los mantenimientos registrados.")
    public ResponseEntity<List<MantenimientoModel>> getAll() {
        return ResponseEntity.ok(mantenimientoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener mantenimiento por ID", description = "Devuelve el mantenimiento correspondiente al ID proporcionado.")
    public ResponseEntity<MantenimientoModel> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(mantenimientoService.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    @Operation(summary = "Crear nuevo mantenimiento", description = "Registra un nuevo mantenimiento en el sistema.")
    public ResponseEntity<MantenimientoModel> create(@RequestBody MantenimientoModel mantenimiento) {
        MantenimientoModel creado = mantenimientoService.save(mantenimiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar mantenimiento", description = "Actualiza un mantenimiento existente usando su ID.")
    public ResponseEntity<MantenimientoModel> update(@PathVariable Long id, @RequestBody MantenimientoModel mantenimiento) {
        try {
            MantenimientoModel actualizado = mantenimientoService.update(id, mantenimiento);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar mantenimiento", description = "Elimina un mantenimiento por su ID.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            mantenimientoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}