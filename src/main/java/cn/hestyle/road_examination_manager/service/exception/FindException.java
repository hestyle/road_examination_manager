package cn.hestyle.road_examination_manager.service.exception;

/**
 * 查找失败
 */
public class FindException extends ServiceException {
    public FindException() {
        super();
    }

    public FindException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FindException(String message, Throwable cause) {
        super(message, cause);
    }

    public FindException(String message) {
        super(message);
    }

    public FindException(Throwable cause) {
        super(cause);
    }
}
