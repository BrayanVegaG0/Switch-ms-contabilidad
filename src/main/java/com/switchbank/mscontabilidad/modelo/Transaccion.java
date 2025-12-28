package com.switchbank.mscontabilidad.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transaccion")
@Getter
@Setter
@NoArgsConstructor
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuentaId", nullable = false)
    private Cuenta cuenta;

    @Column(name = "monto", nullable = false, precision = 19, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoOperacion tipo;

    @Column(name = "referenciaUuid", nullable = false)
    private String referenciaUuid;

    @Column(name = "fechaCreacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void alCrear() {
        fechaCreacion = LocalDateTime.now();
    }

    public Transaccion(Cuenta cuenta, BigDecimal monto, TipoOperacion tipo, String referenciaUuid) {
        this.cuenta = cuenta;
        this.monto = monto;
        this.tipo = tipo;
        this.referenciaUuid = referenciaUuid;
    }
}
