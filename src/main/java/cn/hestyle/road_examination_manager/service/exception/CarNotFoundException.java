package cn.hestyle.road_examination_manager.service.exception;

/**
 * 收货地址数据不存在
 */
public class CarNotFoundException extends ServiceException {

    private static final long serialVersionUID = -6743411944054424816L;

    public CarNotFoundException() {
        super();
    }

    public CarNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CarNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CarNotFoundException(String message) {
        super(message);
    }

    public CarNotFoundException(Throwable cause) {
        super(cause);
    }


}
