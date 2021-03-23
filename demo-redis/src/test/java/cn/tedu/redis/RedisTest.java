package cn.tedu.redis;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisTest {
	/*
	 * 1、连接一个 redis
	 */
	@Test
	public void connectOne() {
		Jedis jedis = new Jedis("192.168.209.101" , 6379);
		System.out.println("connect is ok  ===>"+jedis.ping()); 
	}
	
	/*
	 * 2、通过连接池连接多个 redis
	 */
	@Test
	public void connectByPool() {
		//1、设置连接配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(500);//最大连接数
		//2、构建连接池
		JedisPool pool = new JedisPool(config , "192.168.209.101" , 6379);
		//3、获取连接
		Jedis jedis = pool.getResource();
		String setResult = jedis.set("jedis", "hello , jedis~");
		String value = jedis.get("jedis");
		System.out.println("setResult = "+setResult+" , value = "+value);
		//4、释放连接池
		pool.close();
	}
	
	/*
	 *3、 实现分布式缓存
	 */
	@Test
	public void shard() {
		//1、构建节点信息
		List<JedisShardInfo> infoList = new ArrayList<JedisShardInfo>();
		JedisShardInfo infor01 =new JedisShardInfo("192.168.209.101" , 6379);
		JedisShardInfo infor02 =new JedisShardInfo("192.168.209.101" , 6380);
		infoList.add(infor01);
		infoList.add(infor02);
		//2、分片
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(500);
		
		ShardedJedisPool pool = new ShardedJedisPool(config, infoList);
		ShardedJedis jedis = pool.getResource();
		
		for(int i =0 ; i < 10 ; i++) {
			jedis.getSet("k"+i, "v"+i);
		}
		System.out.println("执行完毕");
		
		//3、释放连接池
		pool.close();
	}
}
