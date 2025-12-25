package com.switchbank.mscontabilidad.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "Cuenta")
@Getter
@Setter
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numerocuenta", nullable = false, unique = true)
    private String numeroCuenta;

    @Column(name = "referenciaclienteid", nullable = false)
    private String referenciaClienteId;

    @Column(name = "saldo", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;

    @Version
    @Column(name = "version")
    private Long version;

    // Constructor vacío (Manual)
    public Cuenta() {
    }

    // Constructor solo con ID (Manual)
    public Cuenta(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Cuenta cuenta = (Cuenta) o;
        return Objects.equals(id, cuenta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "id=" + id +
                ", numeroCuenta='" + numeroCuenta + '\'' +
                ", referenciaClienteId='" + referenciaClienteId + '\'' +
                ", saldo=" + saldo +
                ", version=" + version +
                '}';
    }
}
