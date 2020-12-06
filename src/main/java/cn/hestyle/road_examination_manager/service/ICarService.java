package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.Car;
import cn.hestyle.road_examination_manager.entity.Manager;
import cn.hestyle.road_examination_manager.service.exception.*;

import java.util.List;

/**
 * Car Service层接口
 */
public interface ICarService {
    /**
     *新增车辆
     *
     * @param car 车辆信息
     * @return 新增车辆的数据
     */
    Car addNew(Car car) throws InsertException;

    /**
     * 删除车辆，将id_del置1
     *
     * @param id 要删除的车辆id
     * @throws CarNotFoundException 数据库中没有找到该id对应的车辆信息
     * @throws DeleteException 数据库没有进行修改(影响的行数为1) 访问数据库异常
     */
    void delById(Integer id) throws CarNotFoundException, DeleteException;

    /**
     *
     * @param car
     * @return true:修改成功 false:修改失败（受影响行数!=1）
     * @throws CarNotFoundException 数据库中不存在此id对应的车辆数据
     * @throws AccessDefinedException 访问数据库异常
     */
    Boolean changeInfo(Car car) throws CarNotFoundException, AccessDefinedException;

    /**
     * 分页查询car
     * @param pageIndex 页码（从1开始）
     * @param pageSize 一页大小
     * @return car list
     * @throws PageFindErrorException
     */
    List<Manager> findByPage(Integer pageIndex, Integer pageSize) throws PageFindErrorException;

    /**
     * 获取car的数量
     * @return      car的数量
     */
    Integer getCarCount() throws PageFindErrorException ;
}
