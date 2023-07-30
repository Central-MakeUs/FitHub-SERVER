package fithub.app.batch;

import fithub.app.domain.User;
import fithub.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class monthlySetRecordCountsConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final UserRepository userRepository;

    @Bean
    public Job returnMonthlyRecord(Step MonthlyRecordFirstStep){
        return jobBuilderFactory.get("return Monthly Record")
                .incrementer(new RunIdIncrementer())
                .start(MonthlyRecordFirstStep)
                .build();
    }

    @Bean
    public Step MonthlyRecordFirstStep(ItemReader<User> userItemReader, ItemProcessor<User, User> userItemProcessor, ItemWriter<User> userItemWriter){
        return stepBuilderFactory.get("firstStep")
                .<User, User>chunk(100)
                .reader(userItemReader)
                .processor(userItemProcessor)
                .writer(userItemWriter)
                .build();
    }

    @Bean
    public ItemWriter<User> userItemWriter(){
        return items -> {};
    }

    @Bean
    public ItemReader<User> userItemReader(){
        log.info("Batch Reader ===>");
        List<User> allTargetUsers = userRepository.findAll();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allTargetUsers.size(); i++)
            sb.append(" ").append(allTargetUsers.get(i).getNickname());

        log.info("업데이트 대상 유저 ===> {}", sb);
        return new ListItemReader<>(allTargetUsers);
    }

    @Bean
    public ItemProcessor<User, User> userItemProcessor(){
        return user ->{
            userRepository.resetMonthlyRecordForUsers(user.getId());
            return user;
        };
    }

}
