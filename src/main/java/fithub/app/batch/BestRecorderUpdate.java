package fithub.app.batch;

import fithub.app.domain.BestRecorder;
import fithub.app.domain.User;
import fithub.app.domain.enums.RankingStatus;
import fithub.app.repository.BestRecorderRepository;
import fithub.app.repository.UserExerciseRepository;
import fithub.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BestRecorderUpdate implements ItemProcessor<User, BestRecorder> {

    private final BestRecorderRepository bestRecorderRepository;
    private final UserExerciseRepository userExerciseRepository;
    private List<BestRecorder> result;

    Logger logger = LoggerFactory.getLogger(BestRecorderUpdate.class);

    @Override
    public BestRecorder process(User item) throws Exception {
        List<User> bestUsers = userExerciseRepository.findTopFiveUserExercises(PageRequest.of(0,5)).stream()
                .map(userExercise -> userExercise.getUser()).collect(Collectors.toList());
        StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        List<Long> bestRecorders = (List<Long>) executionContext.get("bestRecorders");

        bestRecorderRepository.deleteAll();

        if(bestRecorders == null || bestRecorders.size() == 0){
            logger.error("최고인증러가 없어!");
            for (int i = 0; i < bestUsers.size(); i++){
                User user = bestUsers.get(i);
                bestRecorderRepository.save(BestRecorder.builder()
                        .userId(user.getId())
                        .ranking(i + 1)
                        .gradeName(user.getBestRecordExercise().getGrade().getName())
                        .level(user.getBestRecordExercise().getGrade().getLevel())
                        .profileUrl(user.getProfileUrl())
                        .recordCount(user.getBestRecordExercise().getRecords())
                        .exerciseName(user.getBestRecordExercise().getExerciseCategory().getName())
                        .nickname(user.getNickname())
                        .rankingStatus(RankingStatus.NEW)
                        .standardDate(LocalDate.now())
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
                                .userId(user.getId())
                                .ranking(i + 1)
                                .gradeName(user.getBestRecordExercise().getGrade().getName())
                                .level(user.getBestRecordExercise().getGrade().getLevel())
                                .profileUrl(user.getProfileUrl())
                                .recordCount(user.getBestRecordExercise().getRecords())
                                .exerciseName(user.getBestRecordExercise().getExerciseCategory().getName())
                                .nickname(user.getNickname())
                                .rankingStatus(RankingStatus.KEEP)
                                .standardDate(LocalDate.now())
                                .build());
                    }
                    else if (i < j) {
                        bestRecorderRepository.save(BestRecorder.builder()
                                .userId(user.getId())
                                .ranking(i + 1)
                                .gradeName(user.getBestRecordExercise().getGrade().getName())
                                .level(user.getBestRecordExercise().getGrade().getLevel())
                                .profileUrl(user.getProfileUrl())
                                .recordCount(user.getBestRecordExercise().getRecords())
                                .exerciseName(user.getBestRecordExercise().getExerciseCategory().getName())
                                .nickname(user.getNickname())
                                .rankingStatus(RankingStatus.UP)
                                .standardDate(LocalDate.now())
                                .build());
                    }
                    else {
                        bestRecorderRepository.save(BestRecorder.builder()
                                .userId(user.getId())
                                .ranking(i + 1)
                                .gradeName(user.getBestRecordExercise().getGrade().getName())
                                .level(user.getBestRecordExercise().getGrade().getLevel())
                                .profileUrl(user.getProfileUrl())
                                .recordCount(user.getBestRecordExercise().getRecords())
                                .exerciseName(user.getBestRecordExercise().getExerciseCategory().getName())
                                .nickname(user.getNickname())
                                .rankingStatus(RankingStatus.DOWN)
                                .standardDate(LocalDate.now())
                                .build());
                    }
                }
            }
            if(!isfound) {
                bestRecorderRepository.save(BestRecorder.builder()
                        .userId(user.getId())
                        .ranking(i + 1)
                        .gradeName(user.getBestRecordExercise().getGrade().getName())
                        .level(user.getBestRecordExercise().getGrade().getLevel())
                        .profileUrl(user.getProfileUrl())
                        .recordCount(user.getBestRecordExercise().getRecords())
                        .exerciseName(user.getBestRecordExercise().getExerciseCategory().getName())
                        .nickname(user.getNickname())
                        .rankingStatus(RankingStatus.NEW)
                        .standardDate(LocalDate.now())
                        .build());
            }
        }}
        return null;
    }
}
