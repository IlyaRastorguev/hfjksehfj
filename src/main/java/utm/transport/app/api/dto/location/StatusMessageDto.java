package utm.transport.app.api.dto.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatusMessageDto {
    private Long statusCode;
    private String Message;
}
