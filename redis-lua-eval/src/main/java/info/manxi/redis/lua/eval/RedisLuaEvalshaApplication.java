package info.manxi.redis.lua.eval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;

@SpringBootApplication
public class RedisLuaEvalshaApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(RedisLuaEvalshaApplication.class, args);

        var redisTemplate = (RedisTemplate<String, String>) context.getBean("redisTemplate");
        redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        redisTemplate.setValueSerializer(StringRedisSerializer.UTF_8);

        redisTemplate.opsForValue().set("name", "Who");
        System.out.println(redisTemplate.opsForValue().get("name"));

        var list = new ArrayList<String>(1);
        list.add("name");
        String execute = redisTemplate.execute(new RedisScript<>() {
            @Override
            public String getSha1() {
                return "aafcf8dc0a5d873bff3ee3e774f603ac754fa572";
            }

            @Override
            public Class getResultType() {
                return String.class;
            }

            @Override
            public String getScriptAsString() {
                return null;
            }
        }, new StringRedisSerializer(), StringRedisSerializer.UTF_8, list, "hello");
        System.out.println(execute);
    }

}
