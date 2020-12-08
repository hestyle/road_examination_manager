package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.controller.exception.RequestException;
import cn.hestyle.road_examination_manager.service.exception.*;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 当前项目中所有控制器类的基类
 */
public abstract class BaseController {
    /**
     * 正确响应的代号
     */
    public static final Integer SUCCESS = 200;
    public static final Integer SUCCESSFUL = 0;
    /**
     * 未知原因的错误响应代号
     */
    public static final Integer FAILURE = -1;

    @ExceptionHandler({ServiceException.class, RequestException.class})// 异常的范围
    @ResponseBody
    public ResponseResult<Void> handleException(Exception e) {

        Integer code = null;
        if (e instanceof ManagerNotFoundException) {
            // 400-用户数据不存在
            code = 400;
        } else if (e instanceof PasswordNotMatchException) {
            // 401-密码错误
            code = 401;
        } else if (e instanceof ManagerNotLoginException) {
            // 402-未登录
            code = 402;
        } else if (e instanceof ManagerAddFailedException) {
            // 403-增加新manager保存错误
            code = 403;
        } else if (e instanceof PageFindErrorException) {
            // 404-分页查询参数错误
            code = 404;
        } else if (e instanceof UpdateException) {
            // 405-数据更新异常
            code = 405;
        } else if (e instanceof DeleteException) {
            // 406-删除异常
            code = 406;
        } else if (e instanceof InsertException) {
            // 407-插入异常
            code = 407;
        } else if (e instanceof FindException) {
            // 408-查询异常
            code = 408;
        }
        return new ResponseResult<>(code, e);
    }

    /**
     * 从Session中获取username
     * @param session   HttpSession对象
     * @return          当前用户登录的username
     */
    protected Integer getUsernameFromSession(HttpSession session) {
        return Integer.valueOf(session.getAttribute("username").toString());
    }
}
