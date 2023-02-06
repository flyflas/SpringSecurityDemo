package icu.xiaobai.book.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import icu.xiaobai.book.entity.UserInfo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final WebUtil webUtil;

    public RedisUtil(StringRedisTemplate redisTemplate, ObjectMapper objectMapper, WebUtil webUtil) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.webUtil = webUtil;
    }

    public String saveUserInfo(UserInfo userInfo) throws JsonProcessingException {
        String token = webUtil.getUUID();
        String key = "user:" + token;
        redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(userInfo));
        redisTemplate.expire(key, 60, TimeUnit.MINUTES);

        return token;
    }

    public UserInfo readUserInfo(String token) throws JsonProcessingException {
        String json = redisTemplate.opsForValue().get("user:" + token);
        if (Objects.isNull(json)) {
            return null;
        }
        return objectMapper.readValue(json, UserInfo.class);
    }

    public void clearUserInfo(String token) {
        redisTemplate.delete("user:" + token);
    }

    public String saveResetPasswordInfo(String email) {
        String link = webUtil.getUUID();
        String key = "reset:password:" + link;
        redisTemplate.opsForValue().set(key, email);
        redisTemplate.expire(key, 5, TimeUnit.MINUTES);

        return link;
    }

    /**
     * email 查找函数
     *  通过密码重置链接中的key，从Redis中查找对应的email
     * @param key 密码重置链接中的唯一标识
     * @return
     *      如果 link 有效, 则返回 email。否则返回 null
     */
    public String getResetPasswordInfo(String key) {
        return redisTemplate.opsForValue().get("reset:password:" + key);
    }

    public String saveAccountActivationInfo(String email) {
        String token = webUtil.getUUID();
        String key = "account:activate:" + token;
        redisTemplate.opsForValue().set(key, email);
        redisTemplate.expire(key, 30, TimeUnit.MINUTES);

        return token;
    }

    public String getAccountActivationInfo(String token) {
        return redisTemplate.opsForValue().get("account:activate:" + token);
    }
}
