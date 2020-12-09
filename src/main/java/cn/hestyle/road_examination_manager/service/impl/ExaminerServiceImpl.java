package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.Examiner;
import cn.hestyle.road_examination_manager.mapper.ExaminerMapper;
import cn.hestyle.road_examination_manager.service.IExaminerService;
import cn.hestyle.road_examination_manager.service.exception.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExaminerServiceImpl implements IExaminerService{
    @Resource
    ExaminerMapper examinerMapper;

    @Override
    public Boolean addExaminer(Examiner examiner){
        // 检查candidate.username是否为空
        if (null == examiner.getId()) {
            throw new ExaminerAddFailedException("账号保存失败，未设置id！");
        }
        // 检查username是否已被注册（username是主键）
        if (null != examinerMapper.findById(examiner.getId())) {
            throw new ExaminerAddFailedException("账号保存失败，身份信息" + examiner.getId() + " 已经被注册！");
        }
        if (examiner.getName() == null || examiner.getName().length() < 2 || examiner.getName().length() > 20) {
            throw new InsertException("增加失败，name字段长度非法，请控制在2-20位！");
        }
        if (examiner.getAge() != null && (examiner.getAge() < 1 || examiner.getAge() > 120)) {
            throw new InsertException("增加失败，age必须大于零，且小于120！");
        }
        if (examiner.getGender() != null && !"男".equals(examiner.getGender()) && !"女".equals(examiner.getGender())) {
            throw new InsertException("增加失败，gender只能为【男】或者【女】！");
        }
        if (examiner.getPhoneNumber() != null && (examiner.getPhoneNumber().length() < 8 || examiner.getName().length() > 11)) {
            throw new InsertException("增加失败，phoneNumber字段长度非法，请控制在8-11位！");
        }
        if (examiner.getIsDel() != null && examiner.getIsDel() != 0 && examiner.getIsDel() != 1) {
            throw new InsertException("增加失败，isDel字段必须为0或1！");
        }
        // 受影响的行数==1，说明插入成功
        try {
            return 1 == examinerMapper.addExaminer(examiner);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExaminerAddFailedException("账号保存失败，数据库发生未知异常！");
        }
    }

    @Override
    public void delExaminer(String id){
        if(findById(id)==null){
            throw new ExaminerNotFoundException("删除考官失败！未找到考官信息");
        }
        examinerMapper.delExaminer(id);
    }

    @Override
    public Examiner findById(String id) {
        return examinerMapper.findById(id);
    }

    @Override
    public List<Examiner> findByPage(Integer pageIndex, Integer pageSize) throws PageFindErrorException {
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
            return examinerMapper.findByPage((pageIndex - 1) * pageSize, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public Integer getExaminerCount() {
        // 调用持久层mapper
        try {
            return examinerMapper.getExaminerCount();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public Boolean modifyBaseInfo(Examiner examiner) throws UpdateException {
        // 通过id查询Candidate
        Examiner managerData = examinerMapper.findById(examiner.getId());
        if (null == managerData) {
            throw new UpdateException("修改失败，" + examiner.getId() + " 用户未注册！");
        }
        // 提取出需要更新的字段，并且判断字段是否合法
//        if (candidate.getName() != null) {
//            if (candidate.getName().length() < 6 || candidate.getName().length() > 20) {
//                throw new UpdateException("修改失败，name字段长度非法，请控制在6-20位！");
//            }
//            managerData.setName(candidate.getName());
//        }
        if (examiner.getAge() != null) {
            if (examiner.getAge() < 1) {
                throw new UpdateException("修改失败，age必须大于零！");
            }
            managerData.setAge(examiner.getAge());
        }
        if (examiner.getGender() != null) {
            if (!"男".equals(examiner.getGender()) && !"女".equals(examiner.getGender())) {
                throw new UpdateException("修改失败，gender只能为【男】或者【女】！");
            }
            managerData.setGender(examiner.getGender());
        }
        if (examiner.getPhoneNumber() != null) {
            if (examiner.getPhoneNumber().length() < 8 || examiner.getName().length() > 11) {
                throw new UpdateException("修改失败，phoneNumber字段长度非法，请控制在8-11位！");
            }
            managerData.setPhoneNumber(examiner.getPhoneNumber());
        }
        if (examiner.getIsDel() != null) {
            if (examiner.getIsDel() != 0 && examiner.getIsDel() != 1) {
                throw new UpdateException("修改失败，isDel字段必须为0或1！");
            }
            managerData.setPhoneNumber(examiner.getPhoneNumber());
        }
        try {
            return 1 == examinerMapper.update(managerData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateException("信息修改失败，数据库发生未知异常！");
        }
    }

    @Override
    public Boolean deleteExaminerByIdList(List<String> idList) throws DeleteException {
        // 首先验证所有username是否都注册
        List<Examiner> examinerList = new ArrayList<>();
        for (String id : idList) {
            Examiner examiner = examinerMapper.findById(id);
            if (examiner == null) {
                throw new DeleteException("批量删除失败，" + id + " 未注册！");
            } else {
                examinerList.add(examiner);
            }
        }
        // 批量删除
        try {
            for (Examiner candidate : examinerList) {
                candidate.setIsDel(1);
                examinerMapper.update(candidate);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DeleteException("批量删除失败，数据库发生未知异常！");
        }
    }

    @Override
    public Boolean modifyPassword(String id, String newPassword, String reNewPassword)  throws UpdateException {
        // 通过username查询manager
        Examiner examiner = examinerMapper.findById(id);
        if (null == examiner) {
            throw new UpdateException("密码修改失败，" + id + " 用户未注册！");
        }
        // 判断两次密码是否一致
        if (null == newPassword || !newPassword.equals(reNewPassword)) {
            throw new UpdateException("密码修改失败！两次密码不一致，请重新输入！");
        }
        // 判断密码长度是否合法
        if (newPassword.length() < 6 || newPassword.length() > 20) {
            throw new UpdateException("密码长度非法，请控制在6-20位！");
        }
        examiner.setPassword(newPassword);
        try {
            return 1 == examinerMapper.update(examiner);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateException("密码修改失败，数据库发生未知异常！");
        }
    }
}
