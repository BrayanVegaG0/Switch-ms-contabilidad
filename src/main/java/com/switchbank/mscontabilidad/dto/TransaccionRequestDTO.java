package com.switchbank.mscontabilidad.dto;

import com.switchbank.mscontabilidad.modelo.TipoOperacion;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TransaccionRequestDTO {

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal monto;

    @NotNull(message = "El tipo de operación es obligatorio (DEBITO, CREDITO)")
    private TipoOperacion tipo;
}
