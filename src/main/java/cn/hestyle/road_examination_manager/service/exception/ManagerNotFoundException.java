package cn.hestyle.road_examination_manager.service.exception;

/**
 * manager不存在
 */
public class ManagerNotFoundException extends ServiceException{
    public ManagerNotFoundException() {
        super();
    }

    public ManagerNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ManagerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerNotFoundException(String message) {
        super(message);
    }

    public ManagerNotFoundException(Throwable cause) {
        super(cause);
    }

}
