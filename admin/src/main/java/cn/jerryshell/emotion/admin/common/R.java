package cn.jerryshell.emotion.admin.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {
    public static final int CODE_OK = 0;
    public static final int CODE_ERROR = 1;

    private int code;
    private String message;
    private T data;

    public static <T> R<T> ok() {
        return ok("OK", null);
    }

    public static <T> R<T> ok(T data) {
        return ok("OK", data);
    }

    public static <T> R<T> ok(String message, T data) {
        return new R<>(CODE_OK, message, data);
    }

    public static <T> R<T> notfound() {
        return notfound("NOTFOUND");
    }

    public static <T> R<T> notfound(String message) {
        return new R<>(CODE_ERROR, message, null);
    }

    public static <T> R<T> error() {
        return error("ERROR");
    }

    public static <T> R<T> error(String message) {
        return new R<>(CODE_ERROR, message, null);
    }
}
