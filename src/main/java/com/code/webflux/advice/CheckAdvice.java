package com.code.webflux.advice;

import com.code.webflux.exceptions.CheckException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

/**
 * Created by yankefei on 2018/12/2.
 * <p>
 * 异常处理切面
 */
@ControllerAdvice
public class CheckAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleBindException(WebExchangeBindException e) {
        return new ResponseEntity<String>(transfStr(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CheckException.class)
    public ResponseEntity<String> handleCheckException(CheckException e) {
        return new ResponseEntity<String>(transfStr(e), HttpStatus.BAD_REQUEST);
    }

    private String transfStr(CheckException e) {
        return e.getFieldName() + "错误的值:" + e.getFieldValue();
    }

    /**
     * 把校验异常转为字符串
     *
     * @param e
     * @return
     */
    private String transfStr(WebExchangeBindException e) {
        return e.getFieldErrors().stream().map(ex -> ex.getField() + ":" + ex.getDefaultMessage())
                .reduce("", (s1, s2) -> s1 + "\n" + s2);
    }

}
