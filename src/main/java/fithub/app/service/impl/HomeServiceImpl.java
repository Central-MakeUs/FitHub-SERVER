package fithub.app.service.impl;

import fithub.app.domain.BestRecorder;
import fithub.app.repository.BestRecorderRepository;
import fithub.app.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeServiceImpl implements HomeService {

    Logger logger = LoggerFactory.getLogger(HomeServiceImpl.class);

    private final BestRecorderRepository bestRecorderRepository;

    @Override
    public List<BestRecorder> findBestRecorderList() {
        List<BestRecorder> bestRecorders = bestRecorderRepository.findAll();
        return bestRecorders;
    }
}
