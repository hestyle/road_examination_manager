package cn.hestyle.road_examination_manager.service.exception;

/**
 * 访问数据权限异常
 */
public class AccessDefinedException extends ServiceException {

    private static final long serialVersionUID = -8400401386129441483L;

    public AccessDefinedException() {
        super();
    }

    public AccessDefinedException(String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AccessDefinedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDefinedException(String message) {
        super(message);
    }

    public AccessDefinedException(Throwable cause) {
        super(cause);
    }


}
