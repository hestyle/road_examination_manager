package cn.hestyle.road_examination_manager.service.exception;

public class ExaminerAddFailedException extends ServiceException {
    public ExaminerAddFailedException() {
    }

    public ExaminerAddFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ExaminerAddFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExaminerAddFailedException(String message) {
        super(message);
    }

    public ExaminerAddFailedException(Throwable cause) {
        super(cause);
    }
}
