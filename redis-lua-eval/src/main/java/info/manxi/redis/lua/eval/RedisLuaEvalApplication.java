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
public class RedisLuaEvalApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(RedisLuaEvalApplication.class, args);

        var redisTemplate = (RedisTemplate<String, String>) context.getBean("redisTemplate");
        redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        redisTemplate.setValueSerializer(StringRedisSerializer.UTF_8);

        redisTemplate.opsForValue().set("name", "Who");
        System.out.println(redisTemplate.opsForValue().get("name"));

        var list = new ArrayList<String>(1);
        list.add("name");

        var script = new DefaultRedisScript<String>();
        script.setResultType(String.class);
        script.setLocation(new ClassPathResource("lua.lua"));
        String execute = redisTemplate.execute(script, StringRedisSerializer.UTF_8, StringRedisSerializer.UTF_8, list, "diss ");
        System.out.println(execute);
    }

}
