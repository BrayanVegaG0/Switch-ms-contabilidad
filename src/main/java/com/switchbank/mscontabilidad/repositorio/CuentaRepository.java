package com.switchbank.mscontabilidad.repositorio;

import com.switchbank.mscontabilidad.modelo.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Integer> {
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
}
