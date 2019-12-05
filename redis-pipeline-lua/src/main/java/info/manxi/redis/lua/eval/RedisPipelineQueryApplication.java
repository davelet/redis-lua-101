package info.manxi.redis.lua.eval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.Instant;

@SpringBootApplication
public class RedisPipelineQueryApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(RedisPipelineQueryApplication.class, args);

        var redisTemplate = (RedisTemplate<String, String>) context.getBean("redisTemplate");
        redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        redisTemplate.setValueSerializer(StringRedisSerializer.UTF_8);

        var start = Instant.now();
        redisTemplate.executePipelined((RedisCallback<String>) redisConnection -> {
            for (var i = 1; i <= 10_0000; i++) {
                redisConnection.get(StringRedisSerializer.UTF_8.serialize("lua_" + i));
            }
            return null;
        });
        System.out.println(Duration.between(start, Instant.now()));
        start = Instant.now();
        redisTemplate.executePipelined((RedisCallback<String>) redisConnection -> {
            for (var i = 1; i <= 10_0000; i++) {
                redisConnection.get(StringRedisSerializer.UTF_8.serialize("lua_" + i));
            }
            return null;
        });
        System.out.println(Duration.between(start, Instant.now()));
    }

}
