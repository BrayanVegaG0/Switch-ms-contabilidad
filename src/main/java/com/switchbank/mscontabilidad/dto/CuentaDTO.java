package com.switchbank.mscontabilidad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDTO {
    private Integer id;
    private String numeroCuenta;
    private String referenciaClienteId;
    private BigDecimal saldo;
}
