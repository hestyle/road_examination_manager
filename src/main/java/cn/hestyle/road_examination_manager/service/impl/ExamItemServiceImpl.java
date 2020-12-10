package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.ExamItem;
import cn.hestyle.road_examination_manager.entity.ExamOperation;
import cn.hestyle.road_examination_manager.mapper.ExamItemMapper;
import cn.hestyle.road_examination_manager.mapper.ExamOperationMapper;
import cn.hestyle.road_examination_manager.service.IExamItemService;
import cn.hestyle.road_examination_manager.service.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
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
        // 检查音频路径合法性
        try {
            if (!checkVoicePath(examItem.getVoicePath())) {
                throw new Exception("音频路径不存在！");
            }
        } catch (Exception e) {
            throw new InsertException("保存失败，" + e.getMessage());
        }
        // 检查operation_ids
        try {
            if (!checkOperationIds(examItem.getOperationIds())) {
                throw new Exception("操作项id列表格式错误！");
            }
        } catch (Exception e) {
            throw new InsertException("保存失败，" + e.getMessage());
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
            if (examItem.getName() == null || examItem.getName().length() == 0) {
                throw new UpdateException("修改失败，考试项name字段必需设置！");
            } else if (examItemMapper.findByName(examItem.getName()) != null) {
                throw new UpdateException("修改失败，考试项name = " + examItem.getName() + "已被注册！");
            } else {
                examItemOriginData.setName(examItem.getName());
            }
        }
//        // 分值不可修改
//        if (!examItemOriginData.getScore().equals(examItem.getScore())) {
//            throw new UpdateException("修改失败，考试项分值不能修改，请恢复至score = " + examItemOriginData.getScore() + "！");
//        }
//        // 检查operation_ids是否合法
//        if (examItem.getOperationIds() == null || examItem.getOperationIds().length() == 0) {
//            throw new InsertException("修改失败，考试项所包含的操作项id必须设置！");
//        }
//        String[] operationIds = examItem.getOperationIds().split(",");
//        for (String idString : operationIds) {
//            if (idString == null || idString.length() == 0) {
//                throw new InsertException("修改失败，考试项operation_ids设置格式错误！");
//            }
//            Integer id = null;
//            try {
//                // 各个id必须是正确的数字，且是有效的operation_id
//                id = Integer.parseInt(idString);
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new InsertException("修改失败，考试项operation_ids设置格式错误！");
//            }
//            if (null == examOperationMapper.findById(id)) {
//                throw new InsertException("修改失败，考试项operation_ids中id = " + id + "未注册！");
//            }
//        }
//        examItemOriginData.setOperationIds(examItem.getOperationIds());
        // 检查description字段
        if (examItem.getDescription() != null && examItem.getDescription().length() != 0) {
            if (examItem.getDescription().length() > 255) {
                throw new UpdateException("修改失败，考试项描述过长，必须控制在1~255个字符！");
            } else {
                examItemOriginData.setDescription(examItem.getDescription());
            }
        } else {
            throw new UpdateException("修改失败，考试项描述不能为空！");
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

    @Override
    public Boolean modifyVoicePath(Integer id, String voicePath) throws UpdateException {
        // 检查id、voicePath
        if (id == null) {
            throw new UpdateException("保存失败，未设置需要修改考试项id！");
        }
        if (voicePath == null || voicePath.length() == 0) {
            throw new UpdateException("保存失败，未设置voicePath！");
        }
        // 检查在resource/static/upload/audio是否存在该文件
        try {
            String pathNameTemp = ResourceUtils.getURL("classpath:").getPath() + "static/upload/audio";
            String pathNameTruth = pathNameTemp.replace("target", "src").replace("classes", "main/resources");
            String filePath = pathNameTruth + voicePath.substring(voicePath.lastIndexOf('/'));
            File file = new File(filePath);
            if (!file.exists()) {
                throw new Exception("文件不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateException("保存失败，voicePath = " + voicePath + " 不存在！");
        }
        // 检查id是否注册
        ExamItem examItem = null;
        try {
            examItem = examItemMapper.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateException("保存失败，数据库发生未知异常！");
        }
        if (examItem == null) {
            throw new UpdateException("保存失败，id = " + id + " 考试项不存在！");
        }
        // 保存修改
        examItem.setVoicePath(voicePath);
        try {
            return 1 == examItemMapper.update(examItem);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateException("保存失败，数据库发生未知异常！");
        }
    }

    @Override
    public Boolean deleteByIdList(List<Integer> idList) throws DeleteException {
        if (idList == null || idList.size() == 0) {
            throw new DeleteException("批量删除失败，未设置需要删除的id list！");
        }
        for (Integer id : idList) {
            ExamItem examItem = examItemMapper.findById(id);
            if (examItem == null) {
                throw new DeleteException("批量删除失败，考试项id = " + id + " 未注册！");
            }
            examItem.setIsDel(1);
            try {
                examItemMapper.update(examItem);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DeleteException("批量删除失败，数据库发生未知异常！");
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 检查voicePath是否合法（音频文件是否存在
     * @param voicePath     音频路径
     * @return              是否合法
     */
    private Boolean checkVoicePath(String voicePath) throws Exception {
        if (voicePath == null || voicePath.length() == 0) {
            throw new Exception("音频路径为空！");
        }
        // 检查在resource/static/upload/audio是否存在该文件
        try {
            String pathNameTemp = ResourceUtils.getURL("classpath:").getPath() + "static/upload/audio";
            String pathNameTruth = pathNameTemp.replace("target", "src").replace("classes", "main/resources");
            String filePath = pathNameTruth + voicePath.substring(voicePath.lastIndexOf('/'));
            File file = new File(filePath);
            if (!file.exists()) {
                throw new Exception(voicePath + "文件不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(voicePath + "文件不存在！");
        }
        return true;
    }

    /**
     * 检查operationIds是否合法（操作项id是否都存在
     * @param operationIdsString        操作项id
     * @return                          是否合法
     */
    private Boolean checkOperationIds(String operationIdsString) throws Exception {
        // 检查operation_ids
        if (operationIdsString == null || operationIdsString.length() == 0) {
            throw new Exception("操作项id列表为空！");
        }
        String[] operationIds = operationIdsString.split(",");
        for (String idString : operationIds) {
            if (idString == null || idString.length() == 0) {
                throw new Exception("操作项id列表格式错误！");
            }
            Integer id = null;
            try {
                // 各个id必须是正确的数字，且是有效的operation_id
                id = Integer.parseInt(idString);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("操作项id列表格式错误！");
            }
            // 检查id是否存在
            ExamOperation examOperation = null;
            try {
                examOperation = examOperationMapper.findById(id);
            } catch (Exception e) {
                throw new Exception("数据库发生未知异常！");
            }
            if (examOperation == null) {
                throw new Exception("考试项operation_ids中id = " + id + "未注册！");
            }
        }
        return true;
    }
}
