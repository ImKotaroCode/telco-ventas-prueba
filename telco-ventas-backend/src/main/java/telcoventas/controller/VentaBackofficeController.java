package telcoventas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import telcoventas.dto.request.RechazarVentaRequest;
import telcoventas.dto.response.VentaResponse;
import telcoventas.service.VentaService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class VentaBackofficeController {

    private final VentaService ventaService;

    @GetMapping("/ventas/pendientes")
    public Page<VentaResponse> pendientes(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "fechaRegistro"));
        return ventaService.pendientes(auth, pageable);
    }

    @PostMapping("/ventas/{id}/aprobar")
    public VentaResponse aprobar(Authentication auth, @PathVariable Long id) {
        return ventaService.aprobar(auth, id);
    }

    @PostMapping("/ventas/{id}/rechazar")
    public VentaResponse rechazar(Authentication auth, @PathVariable Long id, @Valid @RequestBody RechazarVentaRequest req) {
        return ventaService.rechazar(auth, id, req);
    }
}