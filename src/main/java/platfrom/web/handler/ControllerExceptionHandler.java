package platfrom.web.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import platfrom.web.exception.NotFoundException;


/*** コントローラからthrowされるExceptionをハンドルするクラス */
@Component
@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (!(body instanceof ErrorResponse)) {
            body = new ErrorResponse(status.toString(), status.getReasonPhrase());
        }
        return new ResponseEntity<>(body, headers, status);
    }


    /**
     * 404.
     *
     * @param ex throwされたException
     * @param request the current request
     * @return エラーレスポンス
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<Object> handle404(NotFoundException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        ErrorResponse body = new ErrorResponse(HttpStatus.NOT_FOUND.name(),
                HttpStatus.NOT_FOUND.getReasonPhrase());
        HttpStatus status = HttpStatus.NOT_FOUND;

        return this.handleExceptionInternal(ex, body, headers, status, request);
    }


    /**
     * 400.
     *
     * @param ex throwされたException
     * @param request the current request
     * @return エラーレスポンス
     */
    @ExceptionHandler(BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handle400(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        ErrorResponse body = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(),
                HttpStatus.BAD_REQUEST.getReasonPhrase());
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return this.handleExceptionInternal(ex, body, headers, status, request);
    }


    /**
     * 500.
     * 
     * @param ex throwされたException
     * @return エラーレスポンス
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ErrorResponse body = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(body, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * 500.
     *
     * @param ex throwされたException
     * @return エラーレスポンス
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<Object> handleException(Exception ex) {
        ErrorResponse body = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(body, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

