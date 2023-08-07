package fithub.app.batch;

import fithub.app.domain.Notification;
import fithub.app.domain.User;
import fithub.app.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class weeklySetAlarmConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final NotificationRepository notificationRepository;

    @Bean
    public Job deleteWeeklyAlarm(Step WeeklyAlarmFirstStep){
        return jobBuilderFactory.get("delete weekly alarm")
                .incrementer(new RunIdIncrementer())
                .start(WeeklyAlarmFirstStep)
                .build();
    }

    @Bean
    public Step WeeklyAlarmFirstStep(ItemReader<Notification> notificationItemReader, ItemProcessor<Notification, Notification> notificationItemProcessor, ItemWriter<Notification> notificationItemWriter){
        return stepBuilderFactory.get("firstStep")
                .<Notification, Notification>chunk(100)
                .reader(notificationItemReader)
                .processor(notificationItemProcessor)
                .writer(notificationItemWriter)
                .build();
    }

    @Bean
    public ItemWriter<Notification> notificationItemWriter(){
        return items -> {};
    }

    @Bean
    @Transactional
    public ItemReader<Notification> notificationItemReader(){
        log.info("Batch Reader ===>");
        List<Notification> allTargetNotifications = notificationRepository.findAll();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allTargetNotifications.size(); i++)
            sb.append(" ").append(allTargetNotifications.get(i).getNotificationBody());

        log.info("삭제 대상 알람 ===> {}", sb);
        return new ListItemReader<>(allTargetNotifications);
    }

    @Bean
    @Transactional
    public ItemProcessor<Notification, Notification> notificationItemProcessor(){
        return notification ->{
            notificationRepository.delete(notification);
            return null;
        };
    }

}
