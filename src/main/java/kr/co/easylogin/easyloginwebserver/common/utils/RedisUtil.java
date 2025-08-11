package kr.co.easylogin.easyloginwebserver.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> Optional<T> get(String key, Class<T> clazz) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(json -> praseJson(json, clazz));
    }

    public <T> void set(String key, T value) {
        set(key, value, null);
    }

    public <T> void set(String key, T value, Duration ttl) {
        String json = toJson(value);
        if (ttl != null) {
            redisTemplate.opsForValue().set(key, json, ttl);
        } else {
            redisTemplate.opsForValue().set(key, json);
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    private <T> String toJson(T value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Redis serialization error: {}", value, e);
            throw new BusinessException(ResponseCode.SERVER_ERROR);
        }
    }

    private <T> T praseJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Redis deserialization error : {}", json, e);
            throw new BusinessException(ResponseCode.SERVER_ERROR);
        }
    }
}
