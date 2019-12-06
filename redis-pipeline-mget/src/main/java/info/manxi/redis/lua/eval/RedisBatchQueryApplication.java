package info.manxi.redis.lua.eval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class RedisBatchQueryApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(RedisBatchQueryApplication.class, args);

        var redisTemplate = (RedisTemplate<String, String>) context.getBean("redisTemplate");
        redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        redisTemplate.setValueSerializer(StringRedisSerializer.UTF_8);

//        pipeget(redisTemplate);
        pipeget(redisTemplate);

        mget(redisTemplate);
    }

    private static void mget(RedisTemplate<String, String> redisTemplate) {
        var start = Instant.now();
        List<String> list = new ArrayList<>(10_0000);
        for (var i = 1; i <= 10000; i++) {
            list.add("lua_" + i);
        }

        List<String> strings = redisTemplate.opsForValue().multiGet(list);
//        System.out.println(strings);
        System.out.println("mget: " + Duration.between(start, Instant.now()));
    }

    private static void pipeget(RedisTemplate<String, String> redisTemplate) {
        var start = Instant.now();
        List<Object> list = redisTemplate.executePipelined((RedisCallback<String>) redisConnection -> {
            for (var i = 1; i <= 10000; i++) {
                redisConnection.get(StringRedisSerializer.UTF_8.serialize("lua_" + i));
            }
            return null;
        });
        System.out.println(list);
        System.out.println("pipe: " + Duration.between(start, Instant.now()));
    }

}
