package CloudProject.A_meet.global.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.servers.Server;

@OpenAPIDefinition(

        info = @Info(title = "A-meet API",
                description = "2024-2 클라우드 컴퓨팅 프로젝트",
                version = "v1")
)

@Configuration
public class SwaggerConfig {
    @Value("${server.url}")
    private String serverUrl;


    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server().url("https://localhost:8443").description("local URL");
        Server dynamicServer = new Server().url(serverUrl).description("Server URL");

        return new OpenAPI()
            .components(new Components())
            .servers(List.of(localServer, dynamicServer));
    }

}