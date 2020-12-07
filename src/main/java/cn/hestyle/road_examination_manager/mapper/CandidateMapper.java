package cn.hestyle.road_examination_manager.mapper;

import cn.hestyle.road_examination_manager.entity.Candidate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CandidateMapper {
    List<Candidate> findAll();

    void addCandidate(Candidate candidate);

    Candidate findById(String id);

    void delCandidate(String id);
}
