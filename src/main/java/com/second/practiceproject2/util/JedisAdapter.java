package com.second.practiceproject2.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.second.practiceproject2.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.List;

@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool;

    public static void print(int index, Object obj) {
        System.out.println(String.format("%d, %s", index, obj.toString()));
    }

    public static void main(String[] argv) {
        Jedis jedis = new Jedis("redis://localhost:6379/9");//默认连接6379端口，选择9数据库
        jedis.flushDB();//删掉该数据库

        //数据set、get
        jedis.set("hello", "world");
        print(1, jedis.get("hello"));
        jedis.rename("hello", "newhello");
        print(1, jedis.get("newhello"));
        jedis.setex("hello2", 1800, "world");//带过期时间的数据

        //数值操作
        jedis.set("pv", "100");
        jedis.incr("pv");//+1
        jedis.incrBy("pv", 5);//+5
        print(2, jedis.get("pv"));
        jedis.decrBy("pv", 2);//-2
        print(2, jedis.get("pv"));
        print(3, jedis.keys("*"));//打印所有keys

        //list
        String listName = "list";
        jedis.del(listName);
        for (int i = 0; i < 10; ++i) {
            jedis.lpush(listName, "a" + String.valueOf(i));//l开头的为list命令
        }
        print(4, jedis.lrange(listName, 0, 12));
        print(4, jedis.lrange(listName, 0, 3));
        print(5, jedis.llen(listName));//长度
        print(6, jedis.lpop(listName));//弹出
        print(7, jedis.llen(listName));
        print(8, jedis.lrange(listName, 2, 6));//截取数据
        print(9, jedis.lindex(listName, 3));//查找，取指定元素
        print(10, jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "xx"));//定位动态插入
        print(10, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4", "bb"));
        print(11, jedis.lrange(listName, 0 ,12));

        // hash
        String userKey = "userxx";
        jedis.hset(userKey, "name", "jim");//hashset
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "18618181818");
        print(12, jedis.hget(userKey, "name"));//取
        print(13, jedis.hgetAll(userKey));//取全部
        jedis.hdel(userKey, "phone");//删除
        print(14, jedis.hgetAll(userKey));
        print(15, jedis.hexists(userKey, "email"));//是否存在，类似hashmap中的contains
        print(16, jedis.hexists(userKey, "age"));
        print(17, jedis.hkeys(userKey));//取key
        print(18, jedis.hvals(userKey));//取value
        jedis.hsetnx(userKey, "school", "zju");//若不存在则设置新的，若存在则无效
        jedis.hsetnx(userKey, "name", "yxy");
        print(19, jedis.hgetAll(userKey));

        // set
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        for (int i = 0; i < 10; ++i) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i*i));
        }
        print(20, jedis.smembers(likeKey1));//取集合的成员
        print(21, jedis.smembers(likeKey2));
        print(22, jedis.sunion(likeKey1, likeKey2));//求并
        print(23, jedis.sdiff(likeKey1, likeKey2));//求第一个集合有第二个集合没有的
        print(24, jedis.sinter(likeKey1, likeKey2));//求交
        print(25, jedis.sismember(likeKey1, "12"));//查询某元素是否是成员
        print(26, jedis.sismember(likeKey2, "16"));
        jedis.srem(likeKey1, "5");//删除remove某元素
        print(27, jedis.smembers(likeKey1));
        jedis.smove(likeKey2, likeKey1, "25");//从第二个集合中把25移动到第一个集合
        print(28, jedis.smembers(likeKey1));
        print(29, jedis.scard(likeKey1));//集合有多少元素

        //优先队列sorted sets，简写为z，类似堆，元素已排序
        String rankKey = "rankKey";
        jedis.zadd(rankKey, 15, "jim");
        jedis.zadd(rankKey, 60, "Ben");
        jedis.zadd(rankKey, 90, "Lee");
        jedis.zadd(rankKey, 75, "Lucy");
        jedis.zadd(rankKey, 80, "Mei");
        print(30, jedis.zcard(rankKey));//一共几个
        print(31, jedis.zcount(rankKey, 61, 100));//范围内有几个member
        print(32, jedis.zscore(rankKey, "Lucy"));//查某个member的score
        jedis.zincrby(rankKey, 2, "Lucy");//给某个member加分
        print(33, jedis.zscore(rankKey, "Lucy"));
        jedis.zincrby(rankKey, 2, "Luc");//若member写错则相当于新增member
        print(34, jedis.zscore(rankKey, "Luc"));
        print(35, jedis.zrange(rankKey, 0, 100));
        print(36, jedis.zrange(rankKey, 0, 10));//低分值排序0-10名
        print(36, jedis.zrange(rankKey, 1, 3));
        print(36, jedis.zrevrange(rankKey, 1, 3));//高分值排序
        //用zrangeByScoreWithScores取分值范围内的
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "60", "100")) {
            print(37, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }

        print(38, jedis.zrank(rankKey, "Ben"));//从小到大排名
        print(39, jedis.zrevrank(rankKey, "Ben"));//从大到小排名

        String setKey = "zset";
        jedis.zadd(setKey, 1, "a");//多人分值一样
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "e");

        print(40, jedis.zlexcount(setKey, "-", "+"));//所有按字典序排序，-为负无穷+为正无穷
        print(41, jedis.zlexcount(setKey, "(b", "[d"));//(b,d]按字典序排序
        print(42, jedis.zlexcount(setKey, "[b", "[d"));//[b,d]按字典序排序
        jedis.zrem(setKey, "b");//remove
        print(43, jedis.zrange(setKey, 0, 10));
        jedis.zremrangeByLex(setKey, "(c", "+");//按字典序删除范围
        print(44, jedis.zrange(setKey, 0 ,2));

        /*//redis连接池
        JedisPool pool = new JedisPool();
        for (int i = 0; i < 100; ++i) {
            Jedis j = pool.getResource();
            print(45, j.get("pv"));
            j.close();//close相当于归还资源，不写则独占资源卡死
        }*/

        //redis做缓存
        User user = new User();
        user.setName("xx");
        user.setPassword("ppp");
        user.setHeadUrl("a.png");
        user.setSalt("salt");
        user.setId(1);
        print(46, JSONObject.toJSONString(user));
        jedis.set("user1", JSONObject.toJSONString(user));//把用户信息序列化为JSON字符串并存储在redis里

        String value = jedis.get("user1");
        User user2 = JSON.parseObject(value, User.class);//取出字符串并反序列化为信息
        print(47, user2);
        int k = 2;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }


    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }
}
