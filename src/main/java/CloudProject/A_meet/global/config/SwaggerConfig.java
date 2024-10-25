package CloudProject.A_meet.global.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "A-meet API",
                description = "2024-2 클라우드 컴퓨팅 프로젝트",
                version = "v1"),
        servers = {
                @Server(url = "https://localhost:8080", description = "local URL"),
                @Server(url = "https://server-url", description = "Server URL") //추후 설정
        }
)

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components());
    }

}