package info.manxi.redis.pipeline;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.time.Duration;
import java.time.Instant;

/**
 * <pre>
 * <b>Description</b>
 * </pre>
 * <pre>
 * 创建时间 2019-12-04 11:37
 * 所属工程： redis-lua-101  </pre>
 *
 * @author sheldon yhid: 80752866
 */
public class PipelineTest {
    private static final int COMMAND_NUM = 100000;

    public static void main(String[] args) {
        var jedis = new Jedis("127.0.0.1", 6379);
        withoutPipeline(jedis);
        withPipeline(jedis);
        withoutPipeline(jedis);
        withPipeline(jedis);
        withoutPipeline(jedis);
        withPipeline(jedis);
    }

    private static void withoutPipeline(Jedis jedis) {
        var start = Instant.now();
        for (var i = 0; i < COMMAND_NUM; i++) {
            jedis.set("no_pipe_" + i, String.valueOf(i), SetParams.setParams().ex(60));
        }
        var end = Instant.now();
        var cost = Duration.between(start, end);
        System.out.println("withoutPipeline cost : " + cost);
    }

    private static void withPipeline(Jedis jedis) {
        var pipe = jedis.pipelined();
        var start_pipe = Instant.now();
        for (var i = 0; i < COMMAND_NUM; i++) {
            pipe.set("pipe_" + i, String.valueOf(i), SetParams.setParams().ex(60));
        }
        pipe.sync(); // 获取所有的response
        var end_pipe = Instant.now();
        var cost_pipe = Duration.between(start_pipe, end_pipe);
        System.out.println("withPipeline cost : " + cost_pipe);
    }
}
