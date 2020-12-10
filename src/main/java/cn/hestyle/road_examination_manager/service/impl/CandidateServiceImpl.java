package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.Candidate;
import cn.hestyle.road_examination_manager.mapper.CandidateMapper;
import cn.hestyle.road_examination_manager.service.ICandidateService;
import cn.hestyle.road_examination_manager.service.exception.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CandidateServiceImpl implements ICandidateService{
    @Resource
    CandidateMapper candidateMapper;

    @Override
    public Boolean addCandidate(Candidate candidate){
        // 检查candidate.username是否为空
        if (null == candidate.getId()) {
            throw new CandidateAddFailedException("账号保存失败，未设置id！");
        }
        // 检查username是否已被注册（username是主键）
        if (null != candidateMapper.findById(candidate.getId())) {
            throw new CandidateAddFailedException("账号保存失败，用户名" + candidate.getId() + " 已经被注册！");
        }
        if (candidate.getName() == null || candidate.getName().length() < 2 || candidate.getName().length() > 20) {
            throw new InsertException("增加失败，name字段长度非法，请控制在2-20位！");
        }
        if (candidate.getAge() != null && (candidate.getAge() < 1 || candidate.getAge() > 120)) {
            throw new InsertException("增加失败，age必须大于零，且小于120！");
        }
        if(candidate.getDriverSchool() != null && (candidate.getDriverSchool().length()<1 || candidate.getDriverSchool().length()>50)){
            throw new InsertException("增加失败，driverSchool字段长度非法，必须大于1，小于50！");
        }
        if (candidate.getGender() != null && !"男".equals(candidate.getGender()) && !"女".equals(candidate.getGender())) {
            throw new InsertException("增加失败，gender只能为【男】或者【女】！");
        }
        if (candidate.getPhoneNumber() != null && (candidate.getPhoneNumber().length() < 8 || candidate.getName().length() > 11)) {
            throw new InsertException("增加失败，phoneNumber字段长度非法，请控制在8-11位！");
        }
        if (candidate.getIsDel() != null && candidate.getIsDel() != 0 && candidate.getIsDel() != 1) {
            throw new InsertException("增加失败，isDel字段必须为0或1！");
        }
        // 受影响的行数==1，说明插入成功
        try {
            return 1 == candidateMapper.addCandidate(candidate);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CandidateAddFailedException("账号保存失败，数据库发生未知异常！");
        }
    }

    @Override
    public Candidate findById(String id) {
        return candidateMapper.findById(id);
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

    @Override
    public Boolean deleteCandidateByIdList(List<String> idList) throws DeleteException {
        // 首先验证所有username是否都注册
        List<Candidate> candidateList = new ArrayList<>();
        for (String id : idList) {
            Candidate candidate = candidateMapper.findById(id);
            if (candidate == null) {
                throw new DeleteException("批量删除失败，" + id + " 未注册！");
            } else {
                candidateList.add(candidate);
            }
        }
        // 批量删除
        try {
            for (Candidate candidate1 : candidateList) {
                candidate1.setIsDel(1);
                candidateMapper.update(candidate1);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DeleteException("批量删除失败，数据库发生未知异常！");
        }
    }
}
