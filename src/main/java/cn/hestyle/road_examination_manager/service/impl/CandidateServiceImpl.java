package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.Candidate;
import cn.hestyle.road_examination_manager.mapper.CandidateMapper;
import cn.hestyle.road_examination_manager.service.ICandidateService;
import cn.hestyle.road_examination_manager.service.exception.PageFindErrorException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CandidateServiceImpl implements ICandidateService{
    @Resource
    CandidateMapper candidateMapper;

    @Override
    public List<Candidate> candidateList(){
        List<Candidate> list=candidateMapper.findAll();
        return list;
    }

    @Override
    public void addCandidate(Candidate candidate){
        candidateMapper.addCandidate(candidate);
    }

    @Override
    public Candidate findById(String id) {
        return candidateMapper.findById(id);
    }

    @Override
    public void delCandidate(String id) {
        candidateMapper.delCandidate(id);
    }

    @Override
    public List<Candidate> findByPage(Integer pageIndex, Integer pageSize) throws PageFindErrorException {
        // 检查页码是否合法
        if (pageIndex < 1) {
            throw new PageFindErrorException("页码 " + pageIndex + " 非法，必须大于0！");
        }
        // 检查页大小是否合法
        if (pageSize < 1) {
            throw new PageFindErrorException("页大小 " + pageSize + " 非法，必须大于0！");
        }
        // 调用持久层mapper
        try {
            return candidateMapper.findByPage((pageIndex - 1) * pageSize, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public Integer getCandidateCount() {
        // 调用持久层mapper
        try {
            return candidateMapper.getCandidateCount();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }
}
