package telcoventas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import telcoventas.dto.response.ResumenResponse;
import telcoventas.dto.response.VentasPorDiaResponse;
import telcoventas.exception.ForbiddenException;
import telcoventas.exception.NotFoundException;
import telcoventas.model.EstadoVenta;
import telcoventas.model.Rol;
import telcoventas.model.Usuario;
import telcoventas.model.Venta;
import telcoventas.repository.UsuarioRepository;
import telcoventas.repository.VentaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final UsuarioRepository usuarioRepository;
    private final VentaRepository ventaRepository;

    private Usuario authUser(Authentication auth) {
        return usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    public ResumenResponse resumen(Authentication auth, LocalDateTime desde, LocalDateTime hasta) {
        Usuario supervisor = authUser(auth);
        if (supervisor.getRol() != Rol.SUPERVISOR) {
            throw new ForbiddenException("Solo SUPERVISOR puede ver reportes");
        }

        // Si no mandan rango, usa mes actual
        LocalDateTime d = (desde != null) ? desde : LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime h = (hasta != null) ? hasta : LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        List<Venta> ventas = ventaRepository
                .findByAgente_Supervisor_IdAndFechaRegistroBetween(supervisor.getId(), d, h, org.springframework.data.domain.Pageable.unpaged())
                .getContent();

        Map<String, Long> conteos = ventas.stream()
                .collect(Collectors.groupingBy(v -> v.getEstado().name(), Collectors.counting()));

        BigDecimal montoAprobadas = ventas.stream()
                .filter(v -> v.getEstado() == EstadoVenta.APROBADA)
                .map(Venta::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Map<String, List<Venta>> porDia = ventas.stream()
                .collect(Collectors.groupingBy(v -> v.getFechaRegistro().toLocalDate().format(fmt)));

        List<VentasPorDiaResponse> serie = porDia.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    long count = e.getValue().size();
                    BigDecimal monto = e.getValue().stream()
                            .filter(v -> v.getEstado() == EstadoVenta.APROBADA)
                            .map(Venta::getMonto)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new VentasPorDiaResponse(e.getKey(), count, monto);
                })
                .toList();

        return ResumenResponse.builder()
                .conteosPorEstado(conteos)
                .montoTotalAprobadas(montoAprobadas)
                .ventasPorDia(serie)
                .build();
    }
}