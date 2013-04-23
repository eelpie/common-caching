package uk.co.eelpieconsulting.common.caching;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class CachingServiceWrapper <T, U> {

	private static Logger log = Logger.getLogger(CachingServiceWrapper.class);
	
	private static final String NEGATIVE = "negative";
	
	private CachableService<T, U> service;
	private MemcachedCache cache;
	
	@Autowired
	public CachingServiceWrapper(CachableService<T, U> service, MemcachedCache cache) {
		super();
		this.service = service;
		this.cache = cache;
	}

	public U callService(T parameter) {
		final String cacheKey = service.getCacheKeyFor(parameter);

		final U cachedResult = (U) cache.get(cacheKey);
		if (cachedResult != null) {
			log.debug("Cache hit for: " + cacheKey);
			return cachedResult;
		}
		
		log.debug("Cache miss for '" + cacheKey + "' - checking for negative cache hit");
		if (isNegativeCacheHit(cacheKey)) {
			log.debug("Negative cache hit for '" + cacheKey + "'; returning null");
			return null;
		}
		
		log.debug("Cache miss for '" + cacheKey + "' - delegating to real service");
		final U result = service.callService(parameter);
		if (result != null) {
			log.debug("Caching result for :" + cacheKey);
			cache.put(cacheKey, service.getTTL(), result);
			return result;
		}
		
		log.warn("Live service call failed; returning adding negative cache and returning null");
		setNegativeCacheHit(cacheKey);
		return null;
	}
	
	public void decache(T parameter) {
		final String cacheKey = service.getCacheKeyFor(parameter);
		cache.decache(cacheKey);
		cache.decache(negativeCacheKeyFor(cacheKey));
	}
	
	private void setNegativeCacheHit(String cacheKey) {
		cache.put(negativeCacheKeyFor(cacheKey) , service.getTTL(), 1);
	}
	
	private boolean isNegativeCacheHit(String cacheKey) {
		return cache.get(negativeCacheKeyFor(cacheKey)) != null;
	}
	
	private String negativeCacheKeyFor(String cacheKey) {
		return NEGATIVE + cacheKey;
	}
	
}
