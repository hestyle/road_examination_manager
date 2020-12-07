package cn.hestyle.road_examination_manager.service.exception;

public class CandidateNotFoundException extends ServiceException {
    public CandidateNotFoundException() {
    }

    public CandidateNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CandidateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CandidateNotFoundException(String message) {
        super(message);
    }

    public CandidateNotFoundException(Throwable cause) {
        super(cause);
    }
}
