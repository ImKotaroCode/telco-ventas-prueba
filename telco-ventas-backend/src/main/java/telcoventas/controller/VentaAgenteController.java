package telcoventas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import telcoventas.dto.request.CrearVentaRequest;
import telcoventas.dto.response.VentaResponse;
import telcoventas.model.EstadoVenta;
import telcoventas.service.VentaService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class VentaAgenteController {

    private final VentaService ventaService;

    @PostMapping("/ventas")
    public VentaResponse crear(Authentication auth, @Valid @RequestBody CrearVentaRequest req) {
        return ventaService.crearVenta(auth, req);
    }

    @GetMapping("/ventas/mis-ventas")
    public Page<VentaResponse> misVentas(
            Authentication auth,
            @RequestParam(required = false) EstadoVenta estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaRegistro") String sort,
            @RequestParam(defaultValue = "DESC") Sort.Direction dir
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        return ventaService.misVentas(auth, estado, desde, hasta, pageable);
    }
}