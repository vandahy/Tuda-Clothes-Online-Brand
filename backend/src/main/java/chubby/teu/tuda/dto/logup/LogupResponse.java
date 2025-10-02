package chubby.teu.tuda.dto.logup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogupResponse {
    private boolean success;
    private String message;
}
