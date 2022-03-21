package service.exception;

/**
 * 业务逻辑异常
 */
public class BusinessException extends RuntimeException {
    private String code;
    private String message;

    public BusinessException(String code, String msg) {
        super(code + ":" + msg);
        this.code = code;
        this.message = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
