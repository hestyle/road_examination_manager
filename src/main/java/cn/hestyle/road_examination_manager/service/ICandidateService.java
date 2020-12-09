package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.Candidate;

import java.util.List;

public interface ICandidateService {

    Boolean addCandidate(Candidate candidate);

    Candidate findById(String id);

    void delCandidate(String id);

    List<Candidate> findByPage(Integer pageIndex, Integer pageSize);

    Integer getCandidateCount();

    Boolean modifyBaseInfo(Candidate candidate);

    Boolean deleteCandidateByIdList(List<String> idList);
}
