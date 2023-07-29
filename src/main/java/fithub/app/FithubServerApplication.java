package fithub.app;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = { ContextInstanceDataAutoConfiguration.class })
@EnableJpaAuditing
@EnableBatchProcessing
public class FithubServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FithubServerApplication.class, args);
	}

}
