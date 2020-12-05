package cn.hestyle.road_examination_manager.service.exception;

/**
 * 删除数据异常
 *
 * @author 勇哥
 */
public class DeleteException extends ServiceException {

    private static final long serialVersionUID = -7707024503946856639L;

    public DeleteException() {
        super();
    }

    public DeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteException(String message) {
        super(message);
    }

    public DeleteException(Throwable cause) {
        super(cause);
    }


}
