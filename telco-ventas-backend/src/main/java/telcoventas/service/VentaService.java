package telcoventas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import telcoventas.dto.request.CrearVentaRequest;
import telcoventas.dto.request.RechazarVentaRequest;
import telcoventas.dto.response.VentaResponse;
import telcoventas.exception.BadRequestException;
import telcoventas.exception.ForbiddenException;
import telcoventas.exception.NotFoundException;
import telcoventas.model.*;
import telcoventas.repository.UsuarioRepository;
import telcoventas.repository.VentaRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;

    private Usuario authUser(Authentication auth) {
        return usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    private VentaResponse toResponse(Venta v) {
        return VentaResponse.builder()
                .id(v.getId())
                .agenteId(v.getAgente().getId())
                .agenteUsername(v.getAgente().getUsername())
                .dniCliente(v.getDniCliente())
                .nombreCliente(v.getNombreCliente())
                .telefonoCliente(v.getTelefonoCliente())
                .direccionCliente(v.getDireccionCliente())
                .planActual(v.getPlanActual())
                .planNuevo(v.getPlanNuevo())
                .codigoLlamada(v.getCodigoLlamada())
                .producto(v.getProducto())
                .monto(v.getMonto())
                .estado(v.getEstado())
                .motivoRechazo(v.getMotivoRechazo())
                .fechaRegistro(v.getFechaRegistro())
                .fechaValidacion(v.getFechaValidacion())
                .build();
    }

    @Transactional
    public VentaResponse crearVenta(Authentication auth, CrearVentaRequest req) {
        Usuario agente = authUser(auth);

        if (agente.getRol() != Rol.AGENTE) {
            throw new ForbiddenException("Solo AGENTE puede crear ventas");
        }

        Venta v = Venta.builder()
                .agente(agente)
                .dniCliente(req.getDniCliente())
                .nombreCliente(req.getNombreCliente())
                .telefonoCliente(req.getTelefonoCliente())
                .direccionCliente(req.getDireccionCliente())
                .planActual(req.getPlanActual())
                .planNuevo(req.getPlanNuevo())
                .codigoLlamada(req.getCodigoLlamada())
                .producto(req.getProducto())
                .monto(req.getMonto())
                .estado(EstadoVenta.PENDIENTE)
                .fechaRegistro(LocalDateTime.now())
                .build();

        try {
            Venta saved = ventaRepository.save(v);
            return toResponse(saved);
        } catch (Exception e) {
            // por unique de codigo_llamada
            throw new BadRequestException("codigo_llamada ya existe");
        }
    }

    public Page<VentaResponse> misVentas(
            Authentication auth,
            EstadoVenta estado,
            LocalDateTime desde,
            LocalDateTime hasta,
            Pageable pageable
    ) {
        Usuario agente = authUser(auth);
        if (agente.getRol() != Rol.AGENTE) {
            throw new ForbiddenException("Solo AGENTE puede ver mis-ventas");
        }

        boolean hasEstado = estado != null;
        boolean hasRango = (desde != null && hasta != null);

        Page<Venta> page;
        if (hasEstado && hasRango) {
            page = ventaRepository.findByAgente_IdAndEstadoAndFechaRegistroBetween(agente.getId(), estado, desde, hasta, pageable);
        } else if (hasEstado) {
            page = ventaRepository.findByAgente_IdAndEstado(agente.getId(), estado, pageable);
        } else if (hasRango) {
            page = ventaRepository.findByAgente_IdAndFechaRegistroBetween(agente.getId(), desde, hasta, pageable);
        } else {
            page = ventaRepository.findByAgente_Id(agente.getId(), pageable);
        }

        return page.map(this::toResponse);
    }

    public Page<VentaResponse> pendientes(Authentication auth, Pageable pageable) {
        Usuario u = authUser(auth);
        if (u.getRol() != Rol.BACKOFFICE) {
            throw new ForbiddenException("Solo BACKOFFICE puede ver pendientes");
        }
        return ventaRepository.findByEstado(EstadoVenta.PENDIENTE, pageable).map(this::toResponse);
    }

    @Transactional
    public VentaResponse aprobar(Authentication auth, Long id) {
        Usuario u = authUser(auth);
        if (u.getRol() != Rol.BACKOFFICE) {
            throw new ForbiddenException("Solo BACKOFFICE puede aprobar ventas");
        }

        Venta v = ventaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venta no encontrada"));

        if (v.getEstado() != EstadoVenta.PENDIENTE) {
            throw new BadRequestException("Solo se puede aprobar una venta PENDIENTE");
        }

        v.setEstado(EstadoVenta.APROBADA);
        v.setFechaValidacion(LocalDateTime.now());
        v.setMotivoRechazo(null);

        return toResponse(v);
    }

    @Transactional
    public VentaResponse rechazar(Authentication auth, Long id, RechazarVentaRequest req) {
        Usuario u = authUser(auth);
        if (u.getRol() != Rol.BACKOFFICE) {
            throw new ForbiddenException("Solo BACKOFFICE puede rechazar ventas");
        }

        Venta v = ventaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venta no encontrada"));

        if (v.getEstado() != EstadoVenta.PENDIENTE) {
            throw new BadRequestException("Solo se puede rechazar una venta PENDIENTE");
        }

        v.setEstado(EstadoVenta.RECHAZADA);
        v.setFechaValidacion(LocalDateTime.now());
        v.setMotivoRechazo(req.getMotivoRechazo());

        return toResponse(v);
    }

    public Page<VentaResponse> equipo(
            Authentication auth,
            EstadoVenta estado,
            Long agenteId,
            LocalDateTime desde,
            LocalDateTime hasta,
            Pageable pageable
    ) {
        Usuario supervisor = authUser(auth);
        if (supervisor.getRol() != Rol.SUPERVISOR) {
            throw new ForbiddenException("Solo SUPERVISOR puede ver ventas del equipo");
        }

        boolean hasEstado = estado != null;
        boolean hasAgente = agenteId != null;
        boolean hasRango = (desde != null && hasta != null);

        Page<Venta> page;

        if (hasAgente && hasEstado && hasRango) {
            page = ventaRepository.findByAgente_Supervisor_IdAndAgente_IdAndEstadoAndFechaRegistroBetween(supervisor.getId(), agenteId, estado, desde, hasta, pageable);
        } else if (hasAgente && hasEstado) {
            page = ventaRepository.findByAgente_Supervisor_IdAndAgente_IdAndEstado(supervisor.getId(), agenteId, estado, pageable);
        } else if (hasAgente && hasRango) {
            page = ventaRepository.findByAgente_Supervisor_IdAndAgente_IdAndFechaRegistroBetween(supervisor.getId(), agenteId, desde, hasta, pageable);
        } else if (hasAgente) {
            page = ventaRepository.findByAgente_Supervisor_IdAndAgente_Id(supervisor.getId(), agenteId, pageable);
        } else if (hasEstado && hasRango) {
            page = ventaRepository.findByAgente_Supervisor_IdAndEstadoAndFechaRegistroBetween(supervisor.getId(), estado, desde, hasta, pageable);
        } else if (hasEstado) {
            page = ventaRepository.findByAgente_Supervisor_IdAndEstado(supervisor.getId(), estado, pageable);
        } else if (hasRango) {
            page = ventaRepository.findByAgente_Supervisor_IdAndFechaRegistroBetween(supervisor.getId(), desde, hasta, pageable);
        } else {
            page = ventaRepository.findByAgente_Supervisor_Id(supervisor.getId(), pageable);
        }

        return page.map(this::toResponse);
    }
}