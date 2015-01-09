package uk.co.eelpieconsulting.common.caching;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;

public class InMemoryCache implements Cache {

	private final com.google.common.cache.Cache<Object, Object> cache;

	public InMemoryCache() {
		cache = CacheBuilder.newBuilder().
        maximumSize(1000).expireAfterWrite(5, TimeUnit.MINUTES).               
        build();
	}
	
	@Override
	public void put(String key, int ttl, Object value) {
		cache.put(key, value);	// TODO ignores TTL		
	}

	@Override
	public Object get(String key) {
		return cache.getIfPresent(key);
	}

	@Override
	public void decache(String key) {
		cache.invalidate(key);		
	}

}
