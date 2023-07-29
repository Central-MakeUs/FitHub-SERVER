package fithub.app.batch;

import fithub.app.domain.BestRecorder;
import fithub.app.domain.User;
import fithub.app.repository.BestRecorderRepository;
import fithub.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
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
public class dailySetBestUserConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final BestRecoderReader bestRecoderReader;

    private final BestRecorderUpdate bestRecorderUpdate;

    private final UserRepository userRepository;
    private final BestRecorderRepository bestRecorderRepository;

    // for step 1
    @Bean
    public ItemWriter<BestRecorder> BestRecoderWriter(){
        return items -> {};
    }
    // for step 1
    @Bean
    public ItemProcessor<BestRecorder, BestRecorder> BestRecorderProcessor(){
        return bestRecorder ->{
            return bestRecorder;
        };
    }

    @Bean
    public ExecutionContextPromotionListener promotionListener() {
        // Step 1에서 Step 2로 ExecutionContext를 전달하는 Listener를 생성
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[]{"bestRecorders"});
        return listener;
    }

    @Bean
    public Step firstStep(ItemWriter<BestRecorder> BestRecoderWriter, ItemProcessor<BestRecorder, BestRecorder> fileItemProcessor){
        return stepBuilderFactory.get("firstStep")
                .<BestRecorder, BestRecorder>chunk(10)
                .reader(bestRecoderReader)
                .processor(fileItemProcessor)
                .writer(BestRecoderWriter)
                .listener(promotionListener())
                .build();
    }


    // for step 2
    @Bean
    public ItemReader<User> findBestUser(){
        log.info("Batch Reader ===>");
        List<User> allBestUsers = userRepository.findTop5ByOrderByTotalRecordNumDesc();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allBestUsers.size(); i++)
            sb.append(" ").append(allBestUsers.get(i).getId());

        log.info("최고의 유저 목록 ===> {}", sb);
        return new ListItemReader<>(allBestUsers);
    }

    @Bean
    public Step secondStep(ItemReader<User> findBestUser, ItemWriter<BestRecorder> bestRecorderItemWriter){
        return stepBuilderFactory.get("secondStep")
                .<User,BestRecorder>chunk(10)
                .reader(findBestUser)
                .processor(bestRecorderUpdate)
                .writer(bestRecorderItemWriter)
                .build();
    }


    @Bean
    public Job setBestRecorder(Step firstStep, Step secondStep){
        return jobBuilderFactory.get("FileDelete")
                .incrementer(new RunIdIncrementer())
                .start(firstStep)
                .next(secondStep)
                .build();
    }
}
