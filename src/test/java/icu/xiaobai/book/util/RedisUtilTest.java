package icu.xiaobai.book.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class RedisUtilTest {
    @Autowired
    private RedisUtil redisUtil;

    @Test
    void saveUserInfo() {
    }

    @Test
    void readUserInfo() {
    }

    @Test
    void clearUserInfo() {
    }

    @Test
    void saveResetPasswordInfo() {
    }

    @Test
    void getResetPasswordInfo() {
        log.info(redisUtil.getResetPasswordInfo("123"));
    }
}