package com.example.Usuario.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Correo electrónico inválido")
    @Schema(description = "Correo electrónico del usuario para iniciar sesión", example = "juan@mail.com")
    private String correo;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Schema(description = "Contraseña del usuario para iniciar sesión", example = "1234")
    private String password;

    // Getters y Setters
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}