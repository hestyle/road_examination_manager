package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.Candidate;
import cn.hestyle.road_examination_manager.mapper.CandidateMapper;
import cn.hestyle.road_examination_manager.service.ICandidateService;
import cn.hestyle.road_examination_manager.service.exception.PageFindErrorException;
import cn.hestyle.road_examination_manager.service.exception.UpdateException;
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
    public Boolean modifyBaseInfo(Candidate candidate) throws UpdateException {
        // 通过id查询Candidate
        Candidate managerData = candidateMapper.findById(candidate.getId());
        if (null == managerData) {
            throw new UpdateException("修改失败，" + candidate.getId() + " 用户未注册！");
        }
        // 提取出需要更新的字段，并且判断字段是否合法
//        if (candidate.getName() != null) {
//            if (candidate.getName().length() < 6 || candidate.getName().length() > 20) {
//                throw new UpdateException("修改失败，name字段长度非法，请控制在6-20位！");
//            }
//            managerData.setName(candidate.getName());
//        }
        if (candidate.getAge() != null) {
            if (candidate.getAge() < 1) {
                throw new UpdateException("修改失败，age必须大于零！");
            }
            managerData.setAge(candidate.getAge());
        }
        if (candidate.getGender() != null) {
            if (!"男".equals(candidate.getGender()) && !"女".equals(candidate.getGender())) {
                throw new UpdateException("修改失败，gender只能为【男】或者【女】！");
            }
            managerData.setGender(candidate.getGender());
        }
        if (candidate.getPhoneNumber() != null) {
            if (candidate.getPhoneNumber().length() < 8 || candidate.getName().length() > 11) {
                throw new UpdateException("修改失败，phoneNumber字段长度非法，请控制在8-11位！");
            }
            managerData.setPhoneNumber(candidate.getPhoneNumber());
        }
        if (candidate.getIsDel() != null) {
            if (candidate.getIsDel() != 0 && candidate.getIsDel() != 1) {
                throw new UpdateException("修改失败，isDel字段必须为0或1！");
            }
            managerData.setPhoneNumber(candidate.getPhoneNumber());
        }
        if(candidate.getDriverSchool() != null){
            if(candidate.getDriverSchool().length()<=0 || candidate.getDriverSchool().length()>50){
                throw new UpdateException("修改失败，driverSchool字段长度非法，请控制在1-50位");
            }
            managerData.setDriverSchool(candidate.getDriverSchool());
        }
        try {
            return 1 == candidateMapper.update(managerData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateException("信息修改失败，数据库发生未知异常！");
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
