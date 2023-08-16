package fithub.app.batch;

import fithub.app.domain.BestRecorder;
import fithub.app.repository.BestRecorderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BestRecoderReader extends StepExecutionListenerSupport implements ItemReader<BestRecorder> {

    Logger logger = LoggerFactory.getLogger(BestRecoderReader.class);

    private final BestRecorderRepository bestRecorderRepository;

    private List<BestRecorder> allBestRecorder;

    @Override
    public BestRecorder read() {
        if (allBestRecorder == null) {
            allBestRecorder = bestRecorderRepository.findAll();
        }

        List<Long> bestIds = allBestRecorder.stream()
                .map(bestRecorder -> bestRecorder.getUserId()).collect(Collectors.toList());

        StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
        stepExecution.getJobExecution().getExecutionContext().put("bestRecorders", bestIds);

        for(BestRecorder b : allBestRecorder)
            logger.info("이번의 베스트 인증러 : {}", b.getNickname());
        return null;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // Step이 시작되기 전에 데이터를 읽어들여서 ExecutionContext에 저장.
        allBestRecorder = bestRecorderRepository.findAll();
        StringBuilder sb = new StringBuilder();
        for (BestRecorder bestRecorder : allBestRecorder) {
            sb.append(" ").append(bestRecorder.getId());
        }
        stepExecution.getJobExecution().getExecutionContext().put("bestRecorders", allBestRecorder);
        stepExecution.getJobExecution().getExecutionContext().put("selectedRecorders", sb.toString());
    }
}
