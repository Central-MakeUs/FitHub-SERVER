package fithub.app;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication(exclude = { ContextInstanceDataAutoConfiguration.class })
@EnableJpaAuditing
@EnableFeignClients
@EnableBatchProcessing
@EnableRedisRepositories
public class FithubServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FithubServerApplication.class, args);
	}

}
