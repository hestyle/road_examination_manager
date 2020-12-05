package cn.hestyle.road_examination_manager.service.exception;

/**
 * manager账号插入失败，本身数据有问题，或者数据库发生异常
 */
public class ManagerAddFailedException extends ServiceException {
    public ManagerAddFailedException() {
        super();
    }

    public ManagerAddFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ManagerAddFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerAddFailedException(String message) {
        super(message);
    }

    public ManagerAddFailedException(Throwable cause) {
        super(cause);
    }
}
