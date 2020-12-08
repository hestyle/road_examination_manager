package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.Car;
import cn.hestyle.road_examination_manager.entity.Manager;
import cn.hestyle.road_examination_manager.mapper.CarMapper;
import cn.hestyle.road_examination_manager.service.ICarService;
import cn.hestyle.road_examination_manager.service.exception.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarServiceImpl implements ICarService {
    @Resource
    CarMapper carMapper;

    @Override
    public Car addNew(Car car) throws InsertException{
        car.setType(car.getType().toUpperCase());
        car.setMacAddress(car.getMacAddress().toLowerCase());

        Integer rows = -1;
        try {
            rows = carMapper.addNew(car);
        }catch (Exception e){
            throw new InsertException("新增车辆信息失败！数据库异常！");
        }

        if(rows != 1){
            throw new InsertException("新增车辆信息失败！");
        }
        return car;
    }

    @Override
    public void delById(Integer id) throws DeleteException, CarNotFoundException{
        Car data = findById(id);
        if(data == null){
            throw new CarNotFoundException("删除车辆失败!尝试删除的车辆信息不存在");
        }
        try {
            Integer rows = carMapper.deleteById(id);
            if(rows != 1){
                throw new DeleteException("删除车辆数据时发生未知错误!");
            }
        }catch (Exception e){
            throw new DeleteException("删除车辆信息失败！数据库异常！");
        }


    }

    /**
     *根据车辆的id查询匹配的车辆信息
     *
     * @param id 车辆id
     * @return 匹配的车辆信息，如果没有返回null
     */
    private Car findById(Integer id) throws AccessDefinedException {
        try {
            return carMapper.findById(id);
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("根据车辆id查询车辆信息时访问数据库失败！");
        }
    }

    /**
     *根据车辆id修改车辆信息
     *
     * @param car
     * @return
     * @throws CarNotFoundException
     */
    @Override
    public Boolean changeInfo(Car car) throws CarNotFoundException, AccessDefinedException {
        Car data = null;
        try {
            data = findById(car.getId());
        }catch (Exception e){
            throw new AccessDefinedException("更改车辆信息时查询失败！");
        }

        if(data == null){
            throw new CarNotFoundException("要更改的车辆不存在！");
        }

        try {
            return 1 == carMapper.updateInfo(car);
        }catch (Exception e){
            throw new AccessDefinedException("数据库更新失败,发生未知错误！");
        }
    }

    @Override
    public List<Manager> findByPage(Integer pageIndex, Integer pageSize) throws PageFindErrorException {
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
            return carMapper.findByPage((pageIndex - 1) * pageSize, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public Integer getCarCount() throws PageFindErrorException {
        // 调用持久层mapper
        try {
            return carMapper.getCarCount();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public Car getById(Integer id) throws AccessDefinedException, CarNotFoundException{
        Car data = null;
        try {
            data = findById(id);
        }catch (AccessDefinedException e){
            throw e;
        }
        if(data == null){
            throw new CarNotFoundException("符合要求的车辆不存在");
        }
        if(data.getIsDel() == 1){
            throw new CarNotFoundException("车辆已经被删除");
        }
        return data;
    }
}
