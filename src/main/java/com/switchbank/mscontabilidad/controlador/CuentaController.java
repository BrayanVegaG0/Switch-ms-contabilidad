package com.switchbank.mscontabilidad.controlador;

import com.switchbank.mscontabilidad.dto.CuentaDTO;
import com.switchbank.mscontabilidad.dto.TransaccionRequestDTO;
import com.switchbank.mscontabilidad.modelo.TipoOperacion;
import com.switchbank.mscontabilidad.servicio.CuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cuentas")
@RequiredArgsConstructor
@Tag(name = "Controlador de Cuentas", description = "API para gestión de cuentas bancarias y transacciones")
public class CuentaController {

    private final CuentaService cuentaService;

    @Operation(summary = "Obtener cuenta por ID", description = "Recupera detalles de la cuenta incluyendo saldo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CuentaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CuentaDTO> obtenerCuenta(@PathVariable Integer id) {
        return ResponseEntity.ok(cuentaService.obtenerCuentaPorId(id));
    }

    @Operation(summary = "Crear nueva cuenta", description = "Registra una nueva cuenta bancaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CuentaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CuentaDTO> crearCuenta(@RequestBody CuentaDTO cuentaDTO) {
        return ResponseEntity.ok(cuentaService.crearCuenta(cuentaDTO));
    }

    @Operation(summary = "Realizar transacción", description = "Ejecuta un DEBITO o CREDITO en la cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transacción exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CuentaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Saldo insuficiente o datos inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content)
    })
    @PostMapping("/{id}/transacciones")
    public ResponseEntity<CuentaDTO> realizarTransaccion(
            @PathVariable Integer id,
            @Valid @RequestBody TransaccionRequestDTO request) {

        CuentaDTO resultado;
        if (request.getTipo() == TipoOperacion.DEBITO) {
            resultado = cuentaService.debitar(id, request.getMonto());
        } else {
            resultado = cuentaService.acreditar(id, request.getMonto());
        }

        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }
}
