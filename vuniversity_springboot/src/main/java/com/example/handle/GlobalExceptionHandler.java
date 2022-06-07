package com.example.handle;

import com.example.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NullPointerException.class)
    public Result error(NullPointerException e){
        logger.error("发生空指针异常异常！原因是：{}",e.getMessage());
        return Result.error(400,"业务异常！");
    }

    @ExceptionHandler(SQLException.class)
    public Result error(SQLException e) {
        logger.error("发生数据库异常！原因是：{}",e.getMessage());
        return Result.error(400, "数据库异常！");
    }

}
