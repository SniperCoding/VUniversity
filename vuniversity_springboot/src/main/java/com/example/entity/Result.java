package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    // 状态码
    private Integer code;
    // 状态信息
    private String msg;
    // 实体数据
    private Object data;

    /**
     * 成功返回结果，无数据
     *
     * @return
     */
    public static Result ok() {
        return new Result(200, "成功！", null);
    }

    /**
     * 成功返回结果，带返回数据
     *
     * @param data 返回给前端的数据
     * @return
     */
    public static Result ok(Object data) {
        return new Result(200, "成功！", data);
    }


    /**
     * 失败返回结果，无返回数据
     *
     * @return
     */
    public static Result error() {
        return new Result(400, "失败！", null);
    }

    /**
     * 失败返回结果，无返回数据
     *
     * @param code    失败状态码
     * @param message 失败信息描述
     * @return
     */
    public static Result error(Integer code, String message) {
        return new Result(code, message, null);
    }

    /**
     * 失败返回结果，带数据
     *
     * @param code    失败状态码
     * @param message 失败信息描述
     * @param obj     失败后返回的数据
     * @return
     */
    public static Result error(Integer code, String message, Object obj) {
        return new Result(code, message, obj);
    }
}
