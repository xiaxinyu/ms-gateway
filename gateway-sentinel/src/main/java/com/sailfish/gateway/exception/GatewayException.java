package com.sailfish.gateway.exception;


import com.sailfish.gateway.constant.RequestConstants;

/**
 * @author XIAXINYU3
 * @date 2021.3.24
 */
public class GatewayException extends RuntimeException {
    /**
     * Code be used to get the error message, which is defined in the related
     * error constants file
     */
    private String errorCode;

    /**
     * Code be used to get the error message, which is defined in the related
     * error constants file
     */
    private Integer retCode;

    /**
     * Holds the exception message description.
     */
    private String description;

    /**
     * parameters.
     */
    private Object[] parameters;

    /**
     * Root cause of this exception
     */
    private Throwable cause;

    public GatewayException(String description) {
        super(description);
        this.retCode = RequestConstants.FAILURE;
        this.errorCode = "error.gateway.error";
        this.description = description;
    }

    public GatewayException(String errorCode, String description) {
        super(description);
        this.retCode = RequestConstants.FAILURE;
        this.errorCode = errorCode;
        this.description = description;
    }

    public GatewayException(String errorCode, String description, Object[] parameters) {
        super(description);
        this.retCode = RequestConstants.FAILURE;
        this.errorCode = errorCode;
        this.description = description;
        this.parameters = parameters;
    }

    public GatewayException(String errorCode, String description, Object[] parameters, Throwable cause) {
        super(description);
        this.retCode = RequestConstants.FAILURE;
        this.errorCode = errorCode;
        this.description = description;
        this.parameters = parameters;
        this.cause = cause;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    /**
     * {@inheritDoc} overridden:
     *
     * @Date : 2015-12-28
     * @see Throwable#toString()
     */
    @Override
    public String toString() {
        if (this.cause == null) {
            return super.toString();
        } else {
            return super.toString() + "<---- Caused by: " + cause.toString() + " ---->";
        }
    }
}
