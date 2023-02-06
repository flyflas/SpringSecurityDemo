package icu.xiaobai.book.controller;

import icu.xiaobai.book.entity.response.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping
    public CustomResponse<String> test() {
        return new CustomResponse<>(HttpStatus.OK, "hello");
    }

    @PostMapping
    public CustomResponse<String> postTest() {
        return new CustomResponse<>(HttpStatus.OK, "postTest");
    }

}
