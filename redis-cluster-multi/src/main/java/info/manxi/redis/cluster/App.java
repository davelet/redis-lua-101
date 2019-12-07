package info.manxi.redis.cluster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * <b>Description</b>
 * </pre>
 * <pre>
 * 创建时间 2019-12-07 14:12
 * 所属工程： redis-lua-101  </pre>
 *
 * @author sheldon yhid: 80752866
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        var run = SpringApplication.run(App.class);

        var redisTemplate = (RedisTemplate<String, String>) run.getBean("redisTemplate");
        var map = new HashMap<String, String>(100, 1);
        for (var i = 1; i <= 100; i++) {
            map.put("cluster_" + i, "c" + i);
        }
        var start = System.currentTimeMillis();
        redisTemplate.opsForValue().multiSet(map);
        var medium = System.currentTimeMillis();
        List<String> strings = redisTemplate.opsForValue().multiGet(map.keySet());
        //org.springframework.data.redis.connection.jedis.JedisClusterStringCommands.mGet
        var end = System.currentTimeMillis();
        System.out.println(" \n " + (medium - start) + "\n" + (end - medium));

        map.clear();
        for (var i = 1; i <= 100; i++) {
            map.put("{cluster}_" + i, "c" + i);
        }
        start = System.currentTimeMillis();
        redisTemplate.opsForValue().multiSet(map);
        medium = System.currentTimeMillis();
        redisTemplate.opsForValue().multiGet(map.keySet());
        //org.springframework.data.redis.connection.jedis.JedisClusterStringCommands.mGet
        end = System.currentTimeMillis();
        System.out.println(" \n " + (medium - start) + "\n" + (end - medium));
    }
}
