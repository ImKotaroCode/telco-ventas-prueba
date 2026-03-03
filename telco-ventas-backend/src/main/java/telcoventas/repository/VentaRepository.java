package telcoventas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import telcoventas.model.EstadoVenta;
import telcoventas.model.Venta;

import java.time.LocalDateTime;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    Page<Venta> findByAgente_Id(Long agenteId, Pageable pageable);

    Page<Venta> findByAgente_IdAndEstado(Long agenteId, EstadoVenta estado, Pageable pageable);

    Page<Venta> findByAgente_IdAndFechaRegistroBetween(Long agenteId, LocalDateTime desde, LocalDateTime hasta, Pageable pageable);

    Page<Venta> findByAgente_IdAndEstadoAndFechaRegistroBetween(Long agenteId, EstadoVenta estado, LocalDateTime desde, LocalDateTime hasta, Pageable pageable);

    Page<Venta> findByEstado(EstadoVenta estado, Pageable pageable);

    Page<Venta> findByAgente_Supervisor_Id(Long supervisorId, Pageable pageable);

    Page<Venta> findByAgente_Supervisor_IdAndEstado(Long supervisorId, EstadoVenta estado, Pageable pageable);

    Page<Venta> findByAgente_Supervisor_IdAndFechaRegistroBetween(Long supervisorId, LocalDateTime desde, LocalDateTime hasta, Pageable pageable);

    Page<Venta> findByAgente_Supervisor_IdAndEstadoAndFechaRegistroBetween(Long supervisorId, EstadoVenta estado, LocalDateTime desde, LocalDateTime hasta, Pageable pageable);

    Page<Venta> findByAgente_Supervisor_IdAndAgente_Id(Long supervisorId, Long agenteId, Pageable pageable);

    Page<Venta> findByAgente_Supervisor_IdAndAgente_IdAndEstado(Long supervisorId, Long agenteId, EstadoVenta estado, Pageable pageable);

    Page<Venta> findByAgente_Supervisor_IdAndAgente_IdAndFechaRegistroBetween(Long supervisorId, Long agenteId, LocalDateTime desde, LocalDateTime hasta, Pageable pageable);

    Page<Venta> findByAgente_Supervisor_IdAndAgente_IdAndEstadoAndFechaRegistroBetween(Long supervisorId, Long agenteId, EstadoVenta estado, LocalDateTime desde, LocalDateTime hasta, Pageable pageable);
}