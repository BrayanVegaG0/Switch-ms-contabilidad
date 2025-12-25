package com.switchbank.mscontabilidad.servicio;

import com.switchbank.mscontabilidad.dto.CuentaDTO;
import com.switchbank.mscontabilidad.excepcion.CuentaNoEncontradaException;
import com.switchbank.mscontabilidad.excepcion.SaldoInsuficienteException;
import com.switchbank.mscontabilidad.mapper.CuentaMapper;
import com.switchbank.mscontabilidad.modelo.Cuenta;
import com.switchbank.mscontabilidad.modelo.TipoOperacion;
import com.switchbank.mscontabilidad.modelo.Transaccion;
import com.switchbank.mscontabilidad.repositorio.CuentaRepository;
import com.switchbank.mscontabilidad.repositorio.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;
    private final CuentaMapper cuentaMapper;

    @Transactional(readOnly = true)
    public CuentaDTO obtenerCuentaPorId(Integer id) {
        Objects.requireNonNull(id, "El ID de la cuenta no puede ser nulo");
        return cuentaRepository.findById(id)
                .map(cuentaMapper::aDTO)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public CuentaDTO obtenerCuentaPorNumero(String numeroCuenta) {
        Objects.requireNonNull(numeroCuenta, "El número de cuenta no puede ser nulo");
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .map(cuentaMapper::aDTO)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada con numero: " + numeroCuenta));
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public CuentaDTO crearCuenta(CuentaDTO cuentaDTO) {
        Objects.requireNonNull(cuentaDTO, "El DTO de la cuenta no puede ser nulo");
        Cuenta cuenta = cuentaMapper.aEntidad(cuentaDTO);
        if (cuenta.getSaldo() == null) {
            cuenta.setSaldo(BigDecimal.ZERO);
        }
        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        return cuentaMapper.aDTO(cuentaGuardada);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public CuentaDTO debitar(Integer cuentaId, BigDecimal monto) {
        Objects.requireNonNull(cuentaId, "El ID de la cuenta no puede ser nulo");
        Objects.requireNonNull(monto, "El monto no puede ser nulo");

        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada con ID: " + cuentaId));

        if (cuenta.getSaldo().compareTo(monto) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar el débito");
        }

        cuenta.setSaldo(cuenta.getSaldo().subtract(monto));
        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);

        registrarTransaccion(cuentaActualizada, monto, TipoOperacion.DEBITO);

        return cuentaMapper.aDTO(cuentaActualizada);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public CuentaDTO acreditar(Integer cuentaId, BigDecimal monto) {
        Objects.requireNonNull(cuentaId, "El ID de la cuenta no puede ser nulo");
        Objects.requireNonNull(monto, "El monto no puede ser nulo");

        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada con ID: " + cuentaId));

        cuenta.setSaldo(cuenta.getSaldo().add(monto));
        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);

        registrarTransaccion(cuentaActualizada, monto, TipoOperacion.CREDITO);

        return cuentaMapper.aDTO(cuentaActualizada);
    }

    private void registrarTransaccion(Cuenta cuenta, BigDecimal monto, TipoOperacion tipo) {
        Transaccion log = new Transaccion(cuenta, monto, tipo, UUID.randomUUID().toString());
        transaccionRepository.save(log);
    }
}
