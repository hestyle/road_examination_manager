package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.ExamOperation;
import cn.hestyle.road_examination_manager.mapper.ExamOperationMapper;
import cn.hestyle.road_examination_manager.service.IExamOperationService;
import cn.hestyle.road_examination_manager.service.exception.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * examOperation service层实现类
 * @author hestyle
 */
@Service
public class ExamOperationServiceImpl implements IExamOperationService {
    @Resource
    private ExamOperationMapper examOperationMapper;

    @Override
    public Boolean add(ExamOperation examOperation) throws InsertException {
        // 判断name字段是否为空
        if (examOperation.getName() == null || examOperation.getName().length() == 0) {
            throw new InsertException("保存失败，考试操作项name字段不能空！");
        } else if (examOperation.getName().length() < 5 || examOperation.getName().length() > 50) {
            throw new InsertException("保存失败，考试操作项name字段长度非法，请控制在5~50个字符！");
        }
        // 判断name是否被占用
        if (examOperationMapper.findByName(examOperation.getName()) != null) {
            throw new InsertException("保存失败，考试操作项name = " + examOperation.getName() + " 已被占用，请修改再次提交！");
        }
        try {
            return 1 == examOperationMapper.addNew(examOperation);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InsertException("保存失败，数据库发生未知异常！");
        }
    }

    @Override
    public ExamOperation findById(Integer id) throws FindException {
        ExamOperation examOperation = null;
        try {
            examOperation = examOperationMapper.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException("查找失败，数据库发生未知异常！");
        }
        if (examOperation == null) {
            throw new FindException("查找失败，id=" + id + "未注册操作项！");
        }
        return examOperation;
    }

    @Override
    public ExamOperation findByName(String name) throws FindException {
        // 判断name长度
        if (name == null || name.length() == 0) {
            throw new FindException("查找失败，name不能为空！");
        }
        ExamOperation examOperation = null;
        try {
            examOperation = examOperationMapper.findByName(name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException("查找失败，数据库发生未知异常！");
        }
        if (examOperation == null) {
            throw new FindException("查找失败，未查找到操作项！");
        }
        return examOperation;
    }

    @Override
    public List<ExamOperation> findByPage(Integer pageIndex, Integer pageSize) throws PageFindErrorException {
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
            return examOperationMapper.findByPage((pageIndex - 1) * pageSize, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public List<ExamOperation> findByIdsString(String idsString) throws FindException {
        // 检查iDList长度
        if (idsString == null || idsString.length() == 0) {
            throw new FindException("查找失败，未指定需要查找的操作项id！");
        }
        List<Integer> idList = new ArrayList<>();
        // 将idsString转idList
        String[] ids = idsString.split(",");
        for (String idString : ids) {
            if (idString == null || idString.length() == 0) {
                continue;
            }
            try {
                Integer id = Integer.parseInt(idString);
                idList.add(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return findByIdList(idList);
    }

    @Override
    public List<ExamOperation> findByIdList(List<Integer> idList) throws FindException {
        // 检查iDList长度
        if (idList == null || idList.size() == 0) {
            throw new FindException("查找失败，未指定需要查找的操作项id！");
        }
        // 查找所有指定的id
        List<ExamOperation> examOperationList = new ArrayList<>();
        try {
            for (Integer id : idList) {
                ExamOperation examOperation = examOperationMapper.findById(id);
                if (examOperation != null) {
                    examOperationList.add(examOperation);
                }
            }
            return examOperationList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException("查找失败，数据库发生未知异常");
        }
    }

    @Override
    public Integer getExamOperationCount() {
        try {
            return examOperationMapper.getExamOperationCount();
        } catch (Exception e) {
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public Boolean modify(ExamOperation examOperation) throws UpdateException {
        // 判断id是否指定
        if (examOperation == null || examOperation.getId() == null) {
            throw new UpdateException("修改失败，未指定需要修改考试项的id！");
        }
        // 判断id是否注册
        ExamOperation examOperationData = examOperationMapper.findById(examOperation.getId());
        if (examOperationData == null) {
            throw new UpdateException("修改失败，id = " + examOperation.getId() + " 未注册！");
        }
        // 检测name字段
        if (examOperation.getName() == null || examOperation.getName().length() == 0) {
            throw new UpdateException("修改失败，操作项name字段必须设置！");
        } else if (!examOperation.getName().equals(examOperationData.getName())) {
            if (examOperationMapper.findByName(examOperation.getName()) != null) {
                throw new UpdateException("修改失败，操作项name = " + examOperation.getName() + "已被注册！");
            } else {
                examOperationData.setName(examOperation.getName());
            }
        }
        // 检测description长度是否合法
        if (examOperation.getDescription() != null && examOperation.getDescription().length() > 255) {
            throw new UpdateException("修改失败，考试操作项的描述过长，请控制在0~255个字符！");
        } else {
            examOperationData.setDescription(examOperation.getDescription());
        }
        // 检查isDel状态合法性
        if (examOperation.getIsDel() == null || (examOperation.getIsDel() != 0 && examOperation.getIsDel() != 1)) {
            throw new UpdateException("修改失败，考试操作项只有0未删除、1已删除两种状态！");
        } else {
            examOperationData.setIsDel(examOperation.getIsDel());
        }
        try {
            return 1 == examOperationMapper.update(examOperationData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateException("保存失败，数据库发生未知错误！");
        }
    }

    @Override
    public Boolean deleteById(Integer id) throws DeleteException {
        if (id == null) {
            throw new DeleteException("删除失败，未指定需要删除考试操作项id！");
        }
        // 判断id是否注册
        ExamOperation examOperation = examOperationMapper.findById(id);
        if (examOperation == null) {
            throw new DeleteException("删除失败，考试操作项id = " + id + " 未注册！");
        }
        // 判断是否需要删除
        if (examOperation.getIsDel() == 1) {
            throw new DeleteException("删除失败，该考试项已处删除的状态，无需再次删除！");
        } else {
            examOperation.setIsDel(1);
        }
        try {
            return 1 == examOperationMapper.update(examOperation);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DeleteException("删除失败，数据库发生未知异常！");
        }
    }

    @Override
    public Boolean deleteByIdList(List<Integer> idList) throws DeleteException {
        if (idList == null || idList.size() == 0) {
            throw new DeleteException("批量删除失败，未设置需要删除的id list！");
        }
        for (Integer id : idList) {
            ExamOperation examOperation = examOperationMapper.findById(id);
            if (examOperation == null) {
                throw new DeleteException("批量删除失败，考试操作项id = " + id + " 未注册！");
            }
            examOperation.setIsDel(1);
            try {
                examOperationMapper.update(examOperation);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DeleteException("批量删除失败，数据库发生未知异常！");
            }
        }
        return Boolean.TRUE;
    }
}
