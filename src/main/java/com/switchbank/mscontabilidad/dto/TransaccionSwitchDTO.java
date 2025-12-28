package com.switchbank.mscontabilidad.dto;

import com.switchbank.mscontabilidad.modelo.TipoOperacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionSwitchDTO {

    @NotBlank(message = "El número de cuenta es obligatorio")
    private String numeroCuenta;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private BigDecimal monto;

    @NotNull(message = "El tipo de operación es obligatorio (DEBITO, CREDITO)")
    private TipoOperacion tipo;

    // Agregamos esto para que el Switch mande su ID único y evitar duplicados
    @NotBlank(message = "La referencia UUID es obligatoria para trazabilidad")
    private String referenciaUuid;
}