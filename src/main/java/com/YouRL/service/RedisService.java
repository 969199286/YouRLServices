package com.YouRL.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> template;

    @Autowired
    public RedisService(RedisTemplate<String, Object> template) {
        this.template = template;
    }

    public void setLTSandSTL(String longUrl, String shortUrl, long time) {
        template.opsForValue().set(longUrl, shortUrl, time, TimeUnit.MINUTES);
        template.opsForValue().set(shortUrl, longUrl, time, TimeUnit.MINUTES);
    }

    public void expire(String key, long time) {
        template.expire(key, time, TimeUnit.MINUTES);
    }

    public void set(String key, String value) {
        template.opsForValue().set(key, value);
    }

    public void set(String key, String value, long time) {
        if (time > 0) {
            template.opsForValue().set(key, value, time, TimeUnit.MINUTES);
        } else {
            template.opsForValue().set(key, value);
        }
    }

    public Object get(String key) {
        return key == null ? null : template.opsForValue().get(key);
    }
}
