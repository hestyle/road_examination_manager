package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.Candidate;
import cn.hestyle.road_examination_manager.mapper.CandidateMapper;
import cn.hestyle.road_examination_manager.service.ICandidateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CandidateServiceImpl implements ICandidateService{
    @Resource
    CandidateMapper candidateMapper;

    @Override
    public List<Candidate> candidate_list(){
        List<Candidate> list=candidateMapper.findAll();
        return list;
    }

    @Override
    public void addCandidate(Candidate candidate){
        candidateMapper.addCandidate(candidate);
    }
}
