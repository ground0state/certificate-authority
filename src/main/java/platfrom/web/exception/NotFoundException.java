package platfrom.web.exception;

/*** 操作しようとしたリソースが存在しない場合にthrowされるException */
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1234L;

    public NotFoundException(String message) {
        super(message);
    }
}
