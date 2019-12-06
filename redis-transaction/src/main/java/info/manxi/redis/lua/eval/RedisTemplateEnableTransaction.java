package info.manxi.redis.lua.eval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class RedisTemplateEnableTransaction {

    public static void main(String[] args) {
        var context = SpringApplication.run(RedisTemplateEnableTransaction.class, args);

        var redisTemplate = (RedisTemplate<String, String>) context.getBean("redisTemplate");
        var utf8 = StringRedisSerializer.UTF_8;
        redisTemplate.setKeySerializer(utf8);
        redisTemplate.setValueSerializer(utf8);
        redisTemplate.setEnableTransactionSupport(true);// 启用事务 不然执行线程拿不到事务配置会报错

        var start = Instant.now();
        redisTemplate.multi();
        for (var i =0; i < 300; i++) {
            redisTemplate.opsForValue().set("tr_" + i, "1" + i);
        }
        List<Object> exec = redisTemplate.exec();
        System.out.println(exec + " -- " + Duration.between(start, Instant.now()));
        System.out.println(redisTemplate.opsForValue().get("tr_1"));
        System.out.println(redisTemplate.opsForValue().get("tr_2"));

    }

}
