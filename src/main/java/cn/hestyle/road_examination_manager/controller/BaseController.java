package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.RequestException;
import cn.hestyle.road_examination_manager.service.exception.ManagerNotFoundException;
import cn.hestyle.road_examination_manager.service.exception.PasswordNotMatchException;
import cn.hestyle.road_examination_manager.service.exception.ServiceException;
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
