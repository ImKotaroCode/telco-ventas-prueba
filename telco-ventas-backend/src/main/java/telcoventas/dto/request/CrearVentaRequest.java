package telcoventas.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import telcoventas.model.Producto;

import java.math.BigDecimal;

@Getter @Setter
public class CrearVentaRequest {

    @NotBlank
    @Pattern(regexp = "^(\\d{8}|\\d{11})$", message = "dni_cliente debe tener 8 o 11 dígitos")
    private String dniCliente;

    @NotBlank
    private String nombreCliente;

    @NotBlank
    @Pattern(regexp = "^\\d{9}$", message = "telefono_cliente debe tener 9 dígitos")
    private String telefonoCliente;

    @NotBlank
    private String direccionCliente;

    @NotBlank
    private String planActual;

    @NotBlank
    private String planNuevo;

    @NotBlank
    private String codigoLlamada;

    @NotNull
    private Producto producto;

    @NotNull
    @DecimalMin(value = "0.01", message = "monto debe ser mayor a 0")
    private BigDecimal monto;
}