package cn.hestyle.road_examination_manager.controller.exception;

public class AddManagerDataErrorException extends RequestException {
    public AddManagerDataErrorException() {
        super();
    }

    public AddManagerDataErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AddManagerDataErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddManagerDataErrorException(String message) {
        super(message);
    }

    public AddManagerDataErrorException(Throwable cause) {
        super(cause);
    }
}
