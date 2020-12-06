package cn.hestyle.road_examination_manager.service.exception;

/**
 * 分页查询错误（beginIndex或者pageSize非法）
 */
public class PageFindErrorException extends ServiceException {
    public PageFindErrorException() {
        super();
    }

    public PageFindErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PageFindErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageFindErrorException(String message) {
        super(message);
    }

    public PageFindErrorException(Throwable cause) {
        super(cause);
    }
}
