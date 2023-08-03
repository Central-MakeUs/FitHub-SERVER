package fithub.app.service.impl;

import fithub.app.domain.Grade;
import fithub.app.domain.LevelInfo;
import fithub.app.repository.GradeRepository;
import fithub.app.repository.LevelInfoRepository;
import fithub.app.service.RootService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RootServiceImpl implements RootService {

    private final GradeRepository gradeRepository;

    private final LevelInfoRepository levelInfoRepository;

    @Override
    public List<Grade> findAllGrade() {
        return gradeRepository.findAll();
    }

    @Override
    public LevelInfo findLevelInfo() {
        return levelInfoRepository.findById(1L).get();
    }
}
