package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.controller.exception.RequestException;
import cn.hestyle.road_examination_manager.entity.Car;
import cn.hestyle.road_examination_manager.entity.Manager;
import cn.hestyle.road_examination_manager.service.ICarService;
import cn.hestyle.road_examination_manager.service.exception.InsertException;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.List;

import static cn.hestyle.road_examination_manager.controller.BaseController.FAILURE;
import static cn.hestyle.road_examination_manager.controller.BaseController.SUCCESS;

@RestController
@RequestMapping("/car")
public class CarController extends BaseController{
    @Autowired
    private ICarService carService;

    @PostMapping("/add.do")
    public ResponseResult<Car> handleAdd(Car car, HttpSession session){
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }

        carService.addNew(car);
        /**
         * 失败不会返回成功代码，而是抛出异常
         */
        return new ResponseResult<>(SUCCESS, "车辆信息添加成功！");
    }

    @GetMapping("/del.do")
    public ResponseResult<Void> handleDelById(@RequestParam("id")Integer id, HttpSession session){
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }

        carService.delById(id);
        return new ResponseResult<>(SUCCESS, "车辆信息删除成功！");
    }

    @PostMapping("/changeinfo.do")
    public ResponseResult<Void> handleChangeInfo(Car car, HttpSession session){
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        if(car.getId() == null){
            throw new RequestException("修改失败，参数错误！");
        }

        if(carService.changeInfo(car)){
            return new ResponseResult<>(SUCCESS, "修改车辆信息成功！");
        }else {
            return new ResponseResult<>(FAILURE, "修改车辆信息失败！");
        }
    }

    /**
     * 分页查询考试车辆信息 只查询未被删除的
     *
     * @param pageIndex 页码（起始为1，数据库起始为0）
     * @param pageSize 每页数量
     * @param session
     * @return
     */
    @PostMapping("/findByPage.do")
    public ResponseResult<List<Manager>> handleFindByPage(@RequestParam(name="pageIndex", required = false, defaultValue = "1") Integer pageIndex,
                                                          @RequestParam(name="pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                          HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }

        List<Manager> carList = carService.findByPage(pageIndex, pageSize);
//        Integer pageCount = (carService.getCarCount() + pageSize - 1) / pageSize;
        Integer count = carService.getCarCount();
        return new ResponseResult<List<Manager>>(SUCCESS, count, carList, "查询成功！");
    }


    @PostMapping("/findByCarId.do")
    public ResponseResult<Car> handleFindByCarId(@RequestParam(name = "carId") Integer id,
                                                 HttpSession session){
        // 判断是否已经登录过
        // 判断是否已经登录过
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！管理员或考官未登录！请先进行登录");
        }

        Car data= carService.getById(id);
        return new ResponseResult<Car>(SUCCESS, "查询成功！",data);
    }
}
