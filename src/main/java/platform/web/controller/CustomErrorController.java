package platform.web.controller;

import java.lang.invoke.MethodHandles;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import platfrom.web.handler.ErrorResponse;


@RestController
public class CustomErrorController implements ErrorController {

    // Logger
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ErrorResponse errorHandle100(Exception ex, HttpServletRequest request) {

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String message = (String) request.getAttribute("javax.servlet.error.message");

        logger.error(statusCode);

        return new ErrorResponse(statusCode.toString(), message);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
