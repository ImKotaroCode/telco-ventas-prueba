package telcoventas.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorResponse {
    private String timestamp;
    private String path;
    private String error;
    private String message;
}