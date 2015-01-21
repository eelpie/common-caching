package uk.co.eelpieconsulting.common.caching;

import java.io.IOException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.OperationTimeoutException;

import org.apache.log4j.Logger;

public class MemcachedCache implements Cache {
	
	private final static Logger log = Logger.getLogger(MemcachedCache.class);
		
	private final MemcachedClient memcachedClient;
	
	public MemcachedCache(String memcachedUrls) throws IOException {
		this.memcachedClient= new MemcachedClient(AddrUtil.getAddresses(memcachedUrls));		
	}
	
	@Override
	public void put(String key, int ttl, Object value) {
		try {
			memcachedClient.set(key, ttl, value);
		} catch (OperationTimeoutException e) {
			log.warn("Memcache timeout exception on put: " + e);
		}
			
	}

	@Override
	public Object get(String key) {
		try {
			return memcachedClient.get(key);
		} catch (OperationTimeoutException e) {
			log.warn("Memcache timeout exception on get; returning null: " + e);
			return null;
		}
	}
	
	@Override
	public void decache(String key) {
		try {
			memcachedClient.delete(key);
		} catch (OperationTimeoutException e) {
			log.warn("Memcache timeout exception on delete: " + e);
		}
	}
	
}