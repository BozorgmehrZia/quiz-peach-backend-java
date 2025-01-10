package quiz_peach;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@OpenAPIDefinition(info = @Info(title = "Quiz Peach", version = "1.0.0"))
public class QuizPeachApplication {
    public static void main(final String[] args) {
        SpringApplication.run(QuizPeachApplication.class, args);
    }
}