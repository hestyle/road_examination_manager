package cn.hestyle.road_examination_manager.mapper;

import cn.hestyle.road_examination_manager.entity.Candidate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CandidateMapper {

    Integer addCandidate(Candidate candidate);

    Candidate findById(String id);

    void delCandidate(String id);
    /**
     * 分页查询所有manager
     * @param beginIndex    username降序排列
     * @param pageSize      一页大小
     * @return              manager list
     */
    List<Candidate> findByPage(Integer beginIndex, Integer pageSize);

    Integer getCandidateCount();

    Integer update(Candidate candidate);
}
