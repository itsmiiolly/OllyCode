package nl.thijsmolendijk.ollycode.runtime;

/**
 * Exception used to return values from functions quickly
 * @author molenzwiebel
 */
public class ReturnException extends RuntimeException {
	private static final long serialVersionUID = -2681833318552255534L;
	private OCObject ret;

    public ReturnException(OCObject ret) {
        this.ret = ret;
    }

    /**
     * @return the return value
     */
    public OCObject getReturn() {
        return ret;
    }

    /**
     * This method doesn't do anything for performance reasons.
     * @see Throwable#fillInStackTrace()
     */
    public Throwable fillInStackTrace() {
        return this;
    }
}
