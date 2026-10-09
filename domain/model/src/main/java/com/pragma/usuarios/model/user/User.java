package com.pragma.usuarios.model.user;

import com.pragma.usuarios.model.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId; // Importación obligatoria para Sonar

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long id;
    private String name;
    private String lastName;
    private String documentId;
    private String phone;
    private LocalDate birthDate;
    private String email;
    private String password;
    private Role role;

    public boolean isAdult() {
        if (this.birthDate == null) return false;
        // Corrección SonarQube: Especificar el ZoneId explícitamente
        return Period.between(this.birthDate, LocalDate.now(ZoneId.systemDefault())).getYears() >= 18;
    }
}