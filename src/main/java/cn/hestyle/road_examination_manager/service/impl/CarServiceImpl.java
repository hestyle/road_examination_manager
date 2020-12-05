package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.Car;
import cn.hestyle.road_examination_manager.mapper.CarMapper;
import cn.hestyle.road_examination_manager.service.ICarService;
import cn.hestyle.road_examination_manager.service.exception.CarNotFoundException;
import cn.hestyle.road_examination_manager.service.exception.DeleteException;
import cn.hestyle.road_examination_manager.service.exception.InsertException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CarServiceImpl implements ICarService {
    @Resource
    CarMapper carMapper;

    /**
     * 新增车辆信息
     *
     * @param car 车辆信息
     * @return
     */
    @Override
    public Car addNew(Car car) {
        Integer rows = carMapper.addNew(car);
        if(rows != 1){
            throw new InsertException("新增车辆信息失败");
        }
        return car;
    }


    @Override
    public void delById(Integer id) {
        Car data = findById(id);
        if(data == null){
            throw new CarNotFoundException("删除车辆失败!尝试删除的车辆信息不存在");
        }

        Integer rows = carMapper.deleteById(id);
        if(rows != 1){
            throw new DeleteException("删除车辆数据时发生未知错误!");
        }

    }

    /**
     *根据车辆的id查询匹配的车辆信息
     *
     * @param id 车辆id
     * @return 匹配的车辆信息，如果没有返回null
     */
    private Car findById(Integer id) {
        return carMapper.findById(id);
    }
}
