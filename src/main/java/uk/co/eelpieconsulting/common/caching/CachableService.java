package uk.co.eelpieconsulting.common.caching;

public interface CachableService<T, U> {

	public U callService(T parameter);
	
	public String getCacheKeyFor(T parameter);
	
	public int getTTL();
	
}
