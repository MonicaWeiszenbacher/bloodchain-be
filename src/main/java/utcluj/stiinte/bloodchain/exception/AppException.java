package utcluj.stiinte.bloodchain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Getter
public class AppException extends ResponseStatusException {
    
    private Map<String, Object> arguments;

    public AppException(HttpStatusCode status, String errorKey) {
        super(status, errorKey);
    }
    
    public AppException(HttpStatusCode status, String errorKey, Map<String, Object> arguments) {
        super(status, errorKey);
        this.arguments = arguments;
    }
}
