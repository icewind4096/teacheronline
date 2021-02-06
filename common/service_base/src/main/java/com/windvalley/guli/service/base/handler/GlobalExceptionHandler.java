package com.windvalley.guli.service.base.handler;

import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.common.base.result.ResultCodeEnum;
import com.windvalley.guli.common.base.util.ExceptionUtils;
import com.windvalley.guli.service.base.exception.WindvalleyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//控制器切面
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //需要精细捕获的异常，在此处添加

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        log.error(ExceptionUtils.getMessage(e));
        return R.error();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R error(HttpMessageNotReadableException e){
        log.error(ExceptionUtils.getMessage(e));
        return R.setResult(ResultCodeEnum.JSON_PARSE_ERROR);
    }

    @ExceptionHandler(WindvalleyException.class)
    @ResponseBody
    public R error(WindvalleyException e){
        log.error(ExceptionUtils.getMessage(e));
        return R.error().message(e.getMessage()).code(e.getCode());
    }
}
