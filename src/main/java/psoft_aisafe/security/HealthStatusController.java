package psoft_aisafe.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthStatusController {

    private final RequestMappingHandlerMapping handlerMapping;

    public HealthStatusController(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    // 1. Também transformei o status num JSON: {"status": "Sistema AlSafe Operacional!"}
    @GetMapping("/status")
    public Map<String, String> status() {
        return Map.of("status", "Sistema AlSafe Operacional!");
    }

    // 2. Criamos uma pequena "caixa" (record) para estruturar o nosso JSON
    public record EndpointInfo(String rota, String metodoJava) {}

    // 3. O Spring Boot converte automaticamente esta Lista para formato JSON
    @GetMapping("/endpoints")
    public List<EndpointInfo> listarEndpoints() {
        List<EndpointInfo> endpoints = new ArrayList<>();

        handlerMapping.getHandlerMethods().forEach((info, metodo) -> {
            String rota = info.toString().replace("{", "").replace("}", "");
            String metodoJava = metodo.getMethod().getName() + "()";

            // Adicionamos a informação à lista
            endpoints.add(new EndpointInfo(rota, metodoJava));
        });

        return endpoints;
    }
}