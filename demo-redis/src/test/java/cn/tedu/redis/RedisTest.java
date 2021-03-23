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
	 * 1������һ�� redis
	 */
	@Test
	public void connectOne() {
		Jedis jedis = new Jedis("192.168.209.101" , 6379);
		System.out.println("connect is ok  ===>"+jedis.ping());
	}
	
	/*
	 * 2��ͨ�����ӳ����Ӷ�� redis
	 */
	@Test
	public void connectByPool() {
		//1��������������
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(500);//���������
		//2���������ӳ�
		JedisPool pool = new JedisPool(config , "192.168.209.101" , 6379);
		//3����ȡ����
		Jedis jedis = pool.getResource();
		String setResult = jedis.set("jedis", "hello , jedis~");
		String value = jedis.get("jedis");
		System.out.println("setResult = "+setResult+" , value = "+value);
		//4���ͷ����ӳ�
		pool.close();
	}
	
	/*
	 *3�� ʵ�ֲַ�ʽ����
	 */
	@Test
	public void shard() {
		//1�������ڵ���Ϣ
		List<JedisShardInfo> infoList = new ArrayList<JedisShardInfo>();
		JedisShardInfo infor01 =new JedisShardInfo("192.168.209.101" , 6379);
		JedisShardInfo infor02 =new JedisShardInfo("192.168.209.101" , 6380);
		infoList.add(infor01);
		infoList.add(infor02);
		//2����Ƭ
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(500);
		
		ShardedJedisPool pool = new ShardedJedisPool(config, infoList);
		ShardedJedis jedis = pool.getResource();
		
		for(int i =0 ; i < 10 ; i++) {
			jedis.getSet("k"+i, "v"+i);
		}
		System.out.println("ִ�����");
		
		//3���ͷ����ӳ�
		pool.close();
	}
}
