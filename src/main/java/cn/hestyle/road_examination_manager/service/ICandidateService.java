package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.Candidate;

import java.util.List;

public interface ICandidateService {
    List<Candidate> candidate_list();
    void addCandidate(Candidate candidate);
}
