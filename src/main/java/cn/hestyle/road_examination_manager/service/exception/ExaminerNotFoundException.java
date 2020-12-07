package cn.hestyle.road_examination_manager.service.exception;

public class ExaminerNotFoundException extends ServiceException {
    public ExaminerNotFoundException() {
    }

    public ExaminerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ExaminerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExaminerNotFoundException(String message) {
        super(message);
    }

    public ExaminerNotFoundException(Throwable cause) {
        super(cause);
    }
}
