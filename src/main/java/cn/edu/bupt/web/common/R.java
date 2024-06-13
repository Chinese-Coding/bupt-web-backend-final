package cn.edu.bupt.web.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果, 服务端响应的数据最终都会封装成此对象
 *
 * @param <T>
 * @author zfq
 */
@Data
public class R<T> {
    private Integer code; // 编码: 1 成功, 0 和其它数字为失败

    private String msg; // 错误信息

    private T data; // 数据

    private Map map = new HashMap(); // 动态数据

    public static <T> R<T> success(T object) {
        var r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        var r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        return "R {" +
                "code=" + code +
                ", msg='" + msg +
                "}";
    }
}
