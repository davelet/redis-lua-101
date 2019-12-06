package info.manxi.redis.lua.eval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

@SpringBootApplication
public class RedisMsetApplication {

    static StringRedisSerializer utf8 = StringRedisSerializer.UTF_8;

    public static void main(String[] args) {
        var context = SpringApplication.run(RedisMsetApplication.class, args);

        var redisTemplate = (RedisTemplate<String, String>) context.getBean("redisTemplate");
        redisTemplate.setKeySerializer(utf8);
        redisTemplate.setValueSerializer(utf8);

        mset(redisTemplate);
        pipe(redisTemplate);
        mset(redisTemplate);
        pipe(redisTemplate);
        mset(redisTemplate);
        pipe(redisTemplate);
        mset(redisTemplate);
        pipe(redisTemplate);
    }

    private static void mset(RedisTemplate<String, String> redisTemplate) {
        var start = Instant.now();
        var map = new HashMap<String, String>(10_0000, 1);
        for (var i = 1; i <= 10_0000; i++) {
            map.put("lua_" + i, "2" + i);
        }
        redisTemplate.opsForValue().multiSet(map);
        System.out.println("done m: " + Duration.between(start, Instant.now()));
    }

    private static void pipe(RedisTemplate<String, String> redisTemplate) {
        var start = Instant.now();

        redisTemplate.executePipelined((RedisCallback<String>) cn -> {
            for (var i = 1; i <= 10_0000; i++) {
                cn.set(("lua_" + i).getBytes(), "2".getBytes());
            }
            return null;
        });
        System.out.println("done p: " + Duration.between(start, Instant.now()));
    }

}
