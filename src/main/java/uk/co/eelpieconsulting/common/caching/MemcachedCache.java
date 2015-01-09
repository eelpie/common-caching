package uk.co.eelpieconsulting.common.caching;

import java.io.IOException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

public class MemcachedCache implements Cache {
		
	private final MemcachedClient memcachedClient;
	
	public MemcachedCache(String memcachedUrls) throws IOException {
		this.memcachedClient= new MemcachedClient(AddrUtil.getAddresses(memcachedUrls));	// TODO this locks in stale DNS resolutions for the memcache hostnames		
	}
	
	@Override
	public void put(String key, int ttl, Object value) {
		memcachedClient.set(key, ttl, value);			
	}

	@Override
	public Object get(String key) {
		return memcachedClient.get(key);		
	}
	
	@Override
	public void decache(String key) {
		memcachedClient.delete(key);			
	}
	
}
