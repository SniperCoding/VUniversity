package com.example.handle;

import com.example.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public Result error(NullPointerException e) {
        log.error("发生空指针异常异常！原因是：{}", e.getMessage());
        return Result.error(400, "业务异常！");
    }

    @ExceptionHandler(SQLException.class)
    public Result error(SQLException e) {
        log.error("发生数据库异常！原因是：{}", e.getMessage());
        return Result.error(400, "数据库异常！");
    }

    @ExceptionHandler(GlobalException.class)
    public Result error(GlobalException e) {
        log.error("发生全局异常！原因是：{}", e.getMessage());
        return Result.error(400, e.getMessage());
    }

}
