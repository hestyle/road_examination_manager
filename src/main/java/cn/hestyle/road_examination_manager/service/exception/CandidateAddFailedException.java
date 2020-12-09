package cn.hestyle.road_examination_manager.service.exception;

public class CandidateAddFailedException extends ServiceException {
    public CandidateAddFailedException() {
    }

    public CandidateAddFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CandidateAddFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CandidateAddFailedException(String message) {
        super(message);
    }

    public CandidateAddFailedException(Throwable cause) {
        super(cause);
    }
}
