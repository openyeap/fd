package cn.zhumingwu.database.sql.domain.exceptions;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
public class UnknownDialectException extends RuntimeException {
    private static final long serialVersionUID = -3030241047976213273L;

    public UnknownDialectException(String message) {
        super(message);
    }
}
