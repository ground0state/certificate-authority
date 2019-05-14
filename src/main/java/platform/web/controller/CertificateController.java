package platform.web.controller;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;
import platform.domain.entity.db.CertificateInfo;
import platform.domain.entity.other.CertificateAndKey;
import platform.domain.service.RestService;
import platfrom.web.exception.NotFoundException;
import platfrom.web.handler.ErrorResponse;


@RestController
@RequestMapping(value = "/api/v1")
public class CertificateController {

    private final RestService service;

    @Autowired
    public CertificateController(RestService service) {
        this.service = service;
    }

    /**
     * Test.
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CertificateInfo test() {
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setId((long) 1);
        certificateInfo.setCertificate("test");
        certificateInfo.setCreatedAt(null);

        return certificateInfo;
    }

    /**
     * Read a certificate.
     */
    @RequestMapping(value = "/certificates/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CertificateInfo readOne(@PathVariable(value = "id") @Valid @Min(1) @Max(1000) Long id) {
        return this.service.readOne(id);
    }


    /**
     * Read all certificate.
     */
    @RequestMapping(value = "/certificates", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<CertificateInfo> readAll() {
        return this.service.readAll();
    }

    /**
     * Create a certificate.
     */
    @RequestMapping(value = "/certificates", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CertificateAndKey createOne() {
        return this.service.createOne();
    }

    /**
     * Delete a certificate.
     */
    @RequestMapping(value = "/certificates/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteOne(@PathVariable(value = "id") @Valid @Min(1) @Max(1000) Long id) {
        this.service.deleteOne(id);
    }


    /**
     * 404 error handling.
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse Error404() {
        return new ErrorResponse("404", HttpStatus.NOT_FOUND.getReasonPhrase());
    }


    /**
     * 415 error handling.
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ResponseBody
    public ErrorResponse Error415() {
        return new ErrorResponse("415", HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase());
    }


    /**
     * 400 error handling.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleNoHandlerFoundException() {
        return new ErrorResponse("400", HttpStatus.BAD_REQUEST.getReasonPhrase());
    }


    /**
     * 500 error handling.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse Error500() {
        return new ErrorResponse("500", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }
}
