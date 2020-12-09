package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.ExamItem;
import cn.hestyle.road_examination_manager.mapper.ExamItemMapper;
import cn.hestyle.road_examination_manager.mapper.ExamOperationMapper;
import cn.hestyle.road_examination_manager.service.IExamItemService;
import cn.hestyle.road_examination_manager.service.exception.FindException;
import cn.hestyle.road_examination_manager.service.exception.InsertException;
import cn.hestyle.road_examination_manager.service.exception.PageFindErrorException;
import cn.hestyle.road_examination_manager.service.exception.UpdateException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * ExamItem业务实现类
 * @author hestyle
 */
@Service
public class ExamItemServiceImpl implements IExamItemService {
    @Resource
    private ExamItemMapper examItemMapper;
    @Resource
    private ExamOperationMapper examOperationMapper;


    @Override
    public Boolean add(ExamItem examItem) throws InsertException {
        // 检查各个属性合法性
        if (examItem.getName() == null || examItem.getName().length() == 0) {
            throw new InsertException("保存失败，考试项name字段必填");
        }
        if (examItem.getName().length() < 2 || examItem.getName().length() > 50) {
            throw new InsertException("保存失败，考试项name字段长度必须控制在2~50个字符！");
        }
        if (null != examItemMapper.findByName(examItem.getName())) {
            throw new InsertException("保存失败，考试项name = " + examItem.getName() + " 已被注册！");
        }
        if (examItem.getScore() == null || examItem.getScore() < 1 || examItem.getScore() > 100) {
            throw new InsertException("保存失败，考试项分值必须为 1 ~ 100");
        }
        // 检查operation_ids
        if (examItem.getOperationIds() == null || examItem.getOperationIds().length() == 0) {
            throw new InsertException("保存失败，考试项所包含的操作项必须设置！");
        }
        String[] operationIds = examItem.getOperationIds().split(",");
        for (String idString : operationIds) {
            if (idString == null || idString.length() == 0) {
                throw new InsertException("保存失败，考试项operation_ids设置格式错误！");
            }
            Integer id = null;
            try {
                // 各个id必须是正确的数字，且是有效的operation_id
                id = Integer.parseInt(idString);
            } catch (Exception e) {
                e.printStackTrace();
                throw new InsertException("保存失败，考试项operation_ids设置格式错误！");
            }
            if (null == examOperationMapper.findById(id)) {
                throw new InsertException("保存失败，考试项operation_ids中id = " + id + "未注册！");
            }
        }
        if (examItem.getIsDel() == null || (examItem.getIsDel() != 0 && examItem.getIsDel() != 1)) {
            throw new InsertException("保存失败，考试项状态必须设置，且有且只有0未删除、1已删除两种状态！");
        }
        try {
            return 1 == examItemMapper.addNew(examItem);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InsertException("保存失败，数据库发生未知错误！");
        }
    }

    @Override
    public ExamItem findById(Integer id) throws FindException {
        ExamItem examItem = null;
        try {
            examItem = examItemMapper.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException("查找失败，数据库发生未知异常！");
        }
        if (examItem == null) {
            throw new FindException("查找失败，id = " + id + "的考试项未注册！");
        }
        return examItem;
    }

    @Override
    public ExamItem findByName(String name) throws FindException {
        if (name == null || name.length() == 0) {
            throw new FindException("查找失败，查找的name字段不能为空！");
        }
        ExamItem examItem = null;
        try {
            examItem = examItemMapper.findByName(name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException("查找失败，数据库发生未知异常！");
        }
        if (examItem == null) {
            throw new FindException("查找失败，name = " + name + "的考试项未注册！");
        }
        return examItem;
    }

    @Override
    public List<ExamItem> findByPage(Integer pageIndex, Integer pageSize) throws PageFindErrorException {
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
            return examItemMapper.findByPage((pageIndex - 1) * pageSize, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public List<ExamItem> findByIdsString(String idsString) throws FindException {
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
        // 调用findByIdList方法
        return findByIdList(idList);
    }

    @Override
    public List<ExamItem> findByIdList(List<Integer> idList) throws FindException {
        // 检查iDList长度
        if (idList == null || idList.size() == 0) {
            throw new FindException("查找失败，未指定需要查找的考试项id list！");
        }
        // 查找所有指定的id
        List<ExamItem> examItemList = new ArrayList<>();
        try {
            for (Integer id : idList) {
                ExamItem examItem = examItemMapper.findById(id);
                if (examItem != null) {
                    examItemList.add(examItem);
                }
            }
            return examItemList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException("查找失败，数据库发生未知异常");
        }
    }

    @Override
    public Integer getExamItemCount() {
        try {
            return examItemMapper.getExamItemCount();
        } catch (Exception e) {
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public Boolean modify(ExamItem examItem) throws UpdateException {
        // 判断各属性是否合法
        if (examItem.getId() == null) {
            throw new UpdateException("修改失败，未指定需要修改考试项的id！");
        }
        ExamItem examItemOriginData = examItemMapper.findById(examItem.getId());
        if (examItemOriginData == null) {
            throw new UpdateException("修改失败，考试项id = " + examItem.getId() + "未注册！");
        }
        // 如果名称发生了修改，则判断名称是否被注册
        if (!examItemOriginData.getName().equals(examItem.getName())) {
            if (examItem.getName() == null) {
                throw new UpdateException("修改失败，考试项name字段必需设置！");
            } else if (examItemMapper.findByName(examItem.getName()) != null) {
                throw new UpdateException("修改失败，考试项name = " + examItem.getName() + "已被注册！");
            } else {
                examItemOriginData.setName(examItem.getName());
            }
        }
        // 分值不可修改
        if (!examItemOriginData.getScore().equals(examItem.getScore())) {
            throw new UpdateException("修改失败，考试项分值不能修改，请恢复至score = " + examItemOriginData.getScore() + "！");
        }
        // 检查operation_ids是否合法
        if (examItem.getOperationIds() == null || examItem.getOperationIds().length() == 0) {
            throw new InsertException("修改失败，考试项所包含的操作项id必须设置！");
        }
        String[] operationIds = examItem.getOperationIds().split(",");
        for (String idString : operationIds) {
            if (idString == null || idString.length() == 0) {
                throw new InsertException("修改失败，考试项operation_ids设置格式错误！");
            }
            Integer id = null;
            try {
                // 各个id必须是正确的数字，且是有效的operation_id
                id = Integer.parseInt(idString);
            } catch (Exception e) {
                e.printStackTrace();
                throw new InsertException("修改失败，考试项operation_ids设置格式错误！");
            }
            if (null == examOperationMapper.findById(id)) {
                throw new InsertException("修改失败，考试项operation_ids中id = " + id + "未注册！");
            }
        }
        examItemOriginData.setOperationIds(examItem.getOperationIds());
        // 检查description字段
        if (examItem.getDescription() != null) {
            if (examItem.getDescription().length() > 255) {
                throw new UpdateException("修改失败，考试项描述过长，必须控制在1~255个字符！");
            } else {
                examItemOriginData.setDescription(examItem.getDescription());
            }
        }
        // 检查isDel字段
        if (examItem.getIsDel() == null || (examItem.getIsDel() != 0 && examItem.getIsDel() != 1)) {
            throw new InsertException("修改失败，考试项状态必须设置，且有且只有0未删除、1已删除两种状态！");
        } else {
            examItemOriginData.setIsDel(examItem.getIsDel());
        }
        try {
            return 1 == examItemMapper.update(examItemOriginData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateException("修改失败，数据库发生未知异常！");
        }
    }
}
