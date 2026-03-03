package telcoventas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class VentasPorDiaResponse {
    private String fecha;
    private Long count;
    private BigDecimal monto;
}