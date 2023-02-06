package icu.xiaobai.book.entity.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CustomResponse<T> {
    private Integer code;
    private String msg;
    private T data;

    public CustomResponse(HttpStatus status) {
        this.code = status.value();
        this.msg = status.getReasonPhrase();
        this.data = null;
    }

    public CustomResponse(HttpStatus status, T data) {
        this.code = status.value();
        this.msg = status.getReasonPhrase();
        this.data = data;
    }

    public CustomResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public CustomResponse(CustomHttpStatus status) {
        this.code = status.value();
        this.msg = status.getReasonPhrase();
        this.data = null;
    }

    public CustomResponse(CustomHttpStatus status, T data) {
        this.code = status.value();
        this.msg = status.getReasonPhrase();
        this.data = data;
    }

}
