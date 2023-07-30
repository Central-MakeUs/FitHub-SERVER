package fithub.app.service;

import fithub.app.domain.BestRecorder;

import java.util.List;

public interface HomeService {

    List<BestRecorder> findBestRecorderList();
}
