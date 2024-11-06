package CloudProject.A_meet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AMeetApplication {

	public static void main(String[] args) {
		SpringApplication.run(AMeetApplication.class, args);
	}

}
