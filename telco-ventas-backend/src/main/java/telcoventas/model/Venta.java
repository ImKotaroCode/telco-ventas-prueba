package telcoventas.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "ventas",
        uniqueConstraints = @UniqueConstraint(name = "uk_ventas_codigo_llamada", columnNames = "codigo_llamada")
)
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agente_id", nullable = false)
    private Usuario agente;

    @Column(name = "dni_cliente", nullable = false, length = 11)
    private String dniCliente;

    @Column(name = "nombre_cliente", nullable = false, length = 120)
    private String nombreCliente;

    @Column(name = "telefono_cliente", nullable = false, length = 9)
    private String telefonoCliente;

    @Column(name = "direccion_cliente", nullable = false, length = 200)
    private String direccionCliente;

    @Column(name = "plan_actual", nullable = false, length = 80)
    private String planActual;

    @Column(name = "plan_nuevo", nullable = false, length = 80)
    private String planNuevo;

    @Column(name = "codigo_llamada", nullable = false, length = 60)
    private String codigoLlamada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Producto producto;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoVenta estado;

    @Column(name = "motivo_rechazo", length = 250)
    private String motivoRechazo;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_validacion")
    private LocalDateTime fechaValidacion;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}