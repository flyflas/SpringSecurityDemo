package icu.xiaobai.book.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import icu.xiaobai.book.entity.User;
import icu.xiaobai.book.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class JacksonTest {
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testUserInfo() throws JsonProcessingException {
        User user = new User("chenxi", "password", 18, "女", 1);
        UserInfo userInfo = new UserInfo(user);
        log.debug(objectMapper.writeValueAsString(userInfo));
    }
}
