package utcluj.stiinte.bloodchain.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ExceptionHandler extends DefaultErrorAttributes {
    
    private final Map<String, Map<String, String>> translations = new HashMap<>();
    
    @SuppressWarnings("unchecked")
    public ExceptionHandler() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        ObjectMapper objectMapper = new ObjectMapper();
        
        for (Resource resource : resolver.getResources("translations/*.json")) {
            Map<String, String> messages = objectMapper.readValue(resource.getInputStream(), Map.class);
            Optional.ofNullable(resource.getFilename())
                    .map(name -> name.split("\\."))
                    .filter(name -> name.length == 2)
                    .map(name -> name[0])
                    .ifPresent(language -> translations.put(language, messages));
        }
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (!(ex instanceof AppException appException)) {
            return null;
        }

        super.resolveException(request, response, handler, ex);
        String userLanguage = request.getLocale().getLanguage();
        String errorKey = appException.getBody().getDetail();
        String translatedErrorMessage = translations.getOrDefault(userLanguage, translations.get("en")).get(errorKey);

        if (appException.getArguments() != null) {
            for (Map.Entry<String, Object> entry : appException.getArguments().entrySet()) {
                translatedErrorMessage = translatedErrorMessage.replace("{" + entry.getKey() + "}", entry.getValue().toString());
            }
        }
        
        response.setStatus(appException.getStatusCode().value());
        
        return new ModelAndView(new MappingJackson2JsonView(), Map.of("error", translatedErrorMessage));
    }
}
