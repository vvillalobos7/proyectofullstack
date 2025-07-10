package com.example.Usuario.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa a un usuario del sistema AgroTech")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;

    @Email(message = "Correo electrónico inválido")
    @Column(nullable = false, unique = true)
    @Schema(description = "Correo electrónico único del usuario", example = "juan@mail.com")
    private String correo;

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(nullable = false)
    @Schema(description = "Contraseña encriptada del usuario", example = "hashed_password")
    private String password;

    @Column(nullable = false, unique = true)
    @Schema(description = "Número telefónico único", example = "987654321")
    private String telefono;

    @Column(nullable = false)
    @Schema(description = "ID del perfil asociado al usuario", example = "2")
    private Long idperfil;
}