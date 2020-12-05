package cn.hestyle.road_examination_manager.controller.exception;

/**
 * manager未登录，就发生请求
 */
public class ManagerNotLoginException extends RequestException {
    public ManagerNotLoginException() {
        super();
    }

    public ManagerNotLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ManagerNotLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerNotLoginException(String message) {
        super(message);
    }

    public ManagerNotLoginException(Throwable cause) {
        super(cause);
    }
}
