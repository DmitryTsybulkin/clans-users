package exceptions;

import java.text.MessageFormat;

public class UserNotEnoughBalanceException extends RuntimeException {

    public UserNotEnoughBalanceException(String username) {
        super(String.format("User: %s don't have enough balance for operation", username));
    }
}
