package fithub.app.batch;

import fithub.app.domain.BestRecorder;
import fithub.app.domain.User;
import fithub.app.domain.enums.RankingStatus;
import fithub.app.repository.BestRecorderRepository;
import fithub.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BestRecorderUpdate implements ItemProcessor<User, BestRecorder> {

    private final BestRecorderRepository bestRecorderRepository;
    private final UserRepository userRepository;
    private List<BestRecorder> result;

    Logger logger = LoggerFactory.getLogger(BestRecorderUpdate.class);

    @Override
    public BestRecorder process(User item) throws Exception {
        List<User> bestUsers = userRepository.findTop5ByOrderByTotalRecordNumDesc();
        StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        List<Long> bestRecorders = (List<Long>) executionContext.get("bestRecorders");

        bestRecorderRepository.deleteAll();

        if(bestRecorders == null || bestRecorders.size() == 0){
            for (int i = 0; i < bestUsers.size(); i++){
                bestRecorderRepository.save(BestRecorder.builder()
                        .user(bestUsers.get(i))
                        .ranking(i + 1)
                        .rankingStatus(RankingStatus.NEW)
                        .build());
            }
        }
        else{
            for(Long userId : bestRecorders)
                logger.info("기존 스탭에서 가져온 데이터 : {}", userId);
            for(int i = 0; i < bestUsers.size(); i++)
            {
            User user = bestUsers.get(i);
            Boolean isfound = false;
            for (int j = 0; j < bestRecorders.size(); j++){
                if(bestRecorders.get(j).equals(user.getId())){
                    isfound = true;
                    if (i == j) {
                        bestRecorderRepository.save(BestRecorder.builder()
                                .user(user)
                                .ranking(i + 1)
                                .rankingStatus(RankingStatus.KEEP)
                                .build());
                    }
                    else if (i < j) {
                        bestRecorderRepository.save(BestRecorder.builder()
                                .user(user)
                                .ranking(i + 1)
                                .rankingStatus(RankingStatus.UP)
                                .build());
                    }
                    else {
                        bestRecorderRepository.save(BestRecorder.builder()
                                .user(user)
                                .ranking(i + 1)
                                .rankingStatus(RankingStatus.DOWN)
                                .build());
                    }
                }
            }
            if(!isfound) {
                bestRecorderRepository.save(BestRecorder.builder()
                        .user(user)
                        .ranking(i + 1)
                        .rankingStatus(RankingStatus.NEW)
                        .build());
            }
        }}
        return null;
    }
}
