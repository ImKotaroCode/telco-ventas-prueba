package telcoventas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import telcoventas.dto.response.VentaResponse;
import telcoventas.model.EstadoVenta;
import telcoventas.service.VentaService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class VentaSupervisorController {

    private final VentaService ventaService;

    @GetMapping("/ventas/equipo")
    public Page<VentaResponse> equipo(
            Authentication auth,
            @RequestParam(required = false) EstadoVenta estado,
            @RequestParam(required = false) Long agenteId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaRegistro") String sort,
            @RequestParam(defaultValue = "DESC") Sort.Direction dir
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        return ventaService.equipo(auth, estado, agenteId, desde, hasta, pageable);
    }
}