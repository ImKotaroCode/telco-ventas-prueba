package telcoventas.dto.response;

import lombok.Builder;
import lombok.Getter;
import telcoventas.model.EstadoVenta;
import telcoventas.model.Producto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class VentaResponse {
    private Long id;
    private Long agenteId;
    private String agenteUsername;

    private String dniCliente;
    private String nombreCliente;
    private String telefonoCliente;
    private String direccionCliente;
    private String planActual;
    private String planNuevo;

    private String codigoLlamada;
    private Producto producto;
    private BigDecimal monto;

    private EstadoVenta estado;
    private String motivoRechazo;

    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaValidacion;
}