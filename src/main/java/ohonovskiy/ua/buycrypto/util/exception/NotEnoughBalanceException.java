package ohonovskiy.ua.buycrypto.util.exception;

public class NotEnoughBalanceException extends RuntimeException {
    public NotEnoughBalanceException(String message) {
        super(message);
    }

    public NotEnoughBalanceException() {
        super("Order amount must be lower or equal to balance.");
    }
}
