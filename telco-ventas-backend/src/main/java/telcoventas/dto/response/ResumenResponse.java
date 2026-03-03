package telcoventas.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.List;

@Getter
@Builder
public class ResumenResponse {
    private Map<String, Long> conteosPorEstado;
    private BigDecimal montoTotalAprobadas;
    private List<VentasPorDiaResponse> ventasPorDia;
}