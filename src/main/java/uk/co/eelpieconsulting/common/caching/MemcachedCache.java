package uk.co.eelpieconsulting.common.caching;

import java.io.IOException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

public class MemcachedCache {
		
	private final MemcachedClient memcachedClient;
	
	public MemcachedCache(String memcachedUrls) throws IOException {
		this.memcachedClient= new MemcachedClient(AddrUtil.getAddresses(memcachedUrls));		
	}

	public void put(String key, int ttl, Object value) {
		memcachedClient.set(key, ttl, value);			
	}

	public Object get(String key) {
		return memcachedClient.get(key);		
	}
	
	public void decache(String key) {
		memcachedClient.delete(key);			
	}
	
}
