package hu.webuni.catalogservice.securityconfig;

import io.lettuce.core.RedisConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);
    private final RedisTemplate<String, String> redisTemplate;


    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getValueFromRedis(String redisKey) {
        return redisTemplate.opsForValue().get(redisKey);
    }


}
