package info.manxi.redis.pipeline;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
public class JedisTest {
    private static final int COMMAND_NUM = 100000;

    public static void main(String[] args) {
        var jedis = new Jedis("127.0.0.1", 6379);
        withoutPipeline(jedis);
        withPipeline(jedis);
        withMset(jedis);
        withLua(jedis);

        withoutPipeline(jedis);
        withPipeline(jedis);
        withMset(jedis);
        withLua(jedis);

        withoutPipeline(jedis);
        withPipeline(jedis);
        withMset(jedis);
        withLua(jedis);
    }

    private static void withLua(Jedis jedis) {
        var start = Instant.now();
        String lua = readFileContent(JedisTest.class.getResource("/lua.lua").getFile());
        Object result = jedis.evalsha(jedis.scriptLoad(lua), Collections.singletonList("100000"), Collections.emptyList());
        System.out.println(result);
        System.out.println("withlua cost : " + Duration.between(start, Instant.now()));
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

    private static void withMset(Jedis jedis) {
        var start_pipe = Instant.now();
        Map<byte[], byte[]> src = new HashMap<>(10_0000, 1);
        for (var i = 0; i < COMMAND_NUM; i++) {
            src.put(("pipe_" + i).getBytes(), "3".getBytes());
        }
        byte[][] bs = toByteArrays(src);
        jedis.mset(bs);

        var end_pipe = Instant.now();
        var cost_pipe = Duration.between(start_pipe, end_pipe);
        System.out.println("withMset cost : " + cost_pipe);
    }

    private static byte[][] toByteArrays(Map<byte[], byte[]> source) {
        byte[][] result = new byte[source.size() * 2][];
        int index = 0;

        Map.Entry entry;
        for (Iterator var3 = source.entrySet().iterator(); var3.hasNext(); result[index++] = (byte[]) entry.getValue()) {
            entry = (Map.Entry) var3.next();
            result[index++] = (byte[]) entry.getKey();
        }

        return result;
    }

    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuilder sbf = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr).append("\n");
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }
}
