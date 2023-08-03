package fithub.app.service;

import fithub.app.domain.Grade;
import fithub.app.domain.LevelInfo;

import java.util.List;

public interface RootService {

    List<Grade> findAllGrade();

    LevelInfo findLevelInfo();
}
