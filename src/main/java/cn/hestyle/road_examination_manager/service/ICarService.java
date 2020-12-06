package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.Car;
import cn.hestyle.road_examination_manager.entity.Manager;
import cn.hestyle.road_examination_manager.service.exception.CarNotFoundException;
import cn.hestyle.road_examination_manager.service.exception.DeleteException;
import cn.hestyle.road_examination_manager.service.exception.InsertException;

/**
 * Car Service层接口
 */
public interface ICarService {
    /**
     *
     * @param car 车辆信息
     * @return 新增车辆的数据
     */
    Car addNew(Car car) throws InsertException;

    void delById(Integer id) throws CarNotFoundException, DeleteException;
}
