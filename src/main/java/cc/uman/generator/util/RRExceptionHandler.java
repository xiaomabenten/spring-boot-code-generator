package cc.uman.generator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * 异常处理器
 * </p>
 *
 * @author shaohua
 * Email xiaomabenten@gmail.com
 * created by 2018/6/14
 */
@RestControllerAdvice
public class RRExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(RRException.class)
    public Response handleRRException(RRException e) {
        Response response = new Response();
        response.put("code", e.getCode());
        response.put("msg", e.getMessage());

        return response;
    }

    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return Response.error();
    }
}
