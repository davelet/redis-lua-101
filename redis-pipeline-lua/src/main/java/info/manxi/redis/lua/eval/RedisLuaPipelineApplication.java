package info.manxi.redis.lua.eval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

@SpringBootApplication
public class RedisLuaPipelineApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(RedisLuaPipelineApplication.class, args);

        var redisTemplate = (RedisTemplate<String, String>) context.getBean("redisTemplate");
        redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        redisTemplate.setValueSerializer(StringRedisSerializer.UTF_8);

        var list = new ArrayList<String>(1);
        list.add("100000");

        var start = Instant.now();
        var script = new DefaultRedisScript<String>();
        script.setResultType(String.class);
        script.setLocation(new ClassPathResource("lua.lua"));
        String execute = redisTemplate.execute(script, StringRedisSerializer.UTF_8, StringRedisSerializer.UTF_8, list);
        System.out.println(execute + " " + Duration.between(start, Instant.now()));
        start = Instant.now();
        execute = redisTemplate.execute(script, StringRedisSerializer.UTF_8, StringRedisSerializer.UTF_8, list);
        System.out.println(execute + " " + Duration.between(start, Instant.now()));
        start = Instant.now();
        execute = redisTemplate.execute(script, StringRedisSerializer.UTF_8, StringRedisSerializer.UTF_8, list);
        System.out.println(execute + " " + Duration.between(start, Instant.now()));
    }

}
