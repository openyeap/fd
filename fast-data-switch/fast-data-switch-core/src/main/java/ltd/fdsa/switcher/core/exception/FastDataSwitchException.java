package ltd.fdsa.switcher.core.exception;

public class FastDataSwitchException extends RuntimeException {

    private static final long serialVersionUID = 2510267358921118998L;

    private String message;

    public FastDataSwitchException() {
        super();
    }

    public FastDataSwitchException(final String message) {
        super(message);
    }

    public FastDataSwitchException(final Exception e) {
        super(e);
    }

    public FastDataSwitchException(Throwable cause) {
        super(cause);
    }

    public FastDataSwitchException(final String message, final Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return this.message == null ? super.getMessage() : this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
