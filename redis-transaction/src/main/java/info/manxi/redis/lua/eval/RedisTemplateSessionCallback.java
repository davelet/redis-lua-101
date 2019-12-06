package info.manxi.redis.lua.eval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class RedisTemplateSessionCallback {

    public static void main(String[] args) {
        var context = SpringApplication.run(RedisTemplateSessionCallback.class, args);

        var redisTemplate = (RedisTemplate<String, String>) context.getBean("redisTemplate");
        var utf8 = StringRedisSerializer.UTF_8;
        redisTemplate.setKeySerializer(utf8);
        redisTemplate.setValueSerializer(utf8);
        redisTemplate.setEnableTransactionSupport(true);

        List<Object> list = redisTemplate.execute(new SessionCallback<>() {
            @Override
            public List<Object> execute(RedisOperations op) throws DataAccessException {
                System.out.println("原值：" + op.opsForValue().get("name"));
                op.watch("name");
                System.out.println("watch");
                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(10));
                    // 去修改值
                } catch (InterruptedException e) {

                }
                op.multi();
                for (var i = 0; i < 3; i++) {
                    redisTemplate.opsForValue().set("tr_" + i, "1" + i);
                }
                return op.exec();
            }
        });
        System.out.println(list);
        System.out.println(redisTemplate.opsForValue().get("tr_1"));
        System.out.println(redisTemplate.opsForValue().get("tr_2"));

        if (list.isEmpty()) {
            System.out.println("已变更，新值：" + redisTemplate.opsForValue().get("name"));
        }
    }

}
