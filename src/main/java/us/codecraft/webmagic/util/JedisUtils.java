package us.codecraft.webmagic.util;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JedisUtils {
    private static Map<JedisHostPort, JedisPool> map = new HashMap();

    public static JedisPool getJedisPool(String host, Integer port, String password) {
        JedisHostPort jedisHostPort = new JedisHostPort(host, port);
        JedisPool jedisPool = map.get(jedisHostPort);
        if (jedisPool!=null) return jedisPool;
        jedisPool = createJedisPool(host, port, password);
        map.put(jedisHostPort, jedisPool);
        return jedisPool;
    }

    private static JedisPool createJedisPool(String host, Integer port, String password) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(30);// 最大连接数，连接全部用完，进行等待
        poolConfig.setMinIdle(10); // 最小空余数
        poolConfig.setMaxIdle(20); // 最大空余数
        return new JedisPool(poolConfig, host,  port, 3000, password);
    }

    public static class JedisHostPort {
        private String host;
        private Integer port;

        public JedisHostPort() { }

        public JedisHostPort(String host, Integer port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof JedisHostPort)) return false;
            JedisHostPort that = (JedisHostPort) o;
            return Objects.equals(getHost(), that.getHost()) &&
                    Objects.equals(getPort(), that.getPort());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getHost(), getPort());
        }
    }
}