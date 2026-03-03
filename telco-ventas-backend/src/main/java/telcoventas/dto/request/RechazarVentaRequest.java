package telcoventas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RechazarVentaRequest {
    @NotBlank
    private String motivoRechazo;
}