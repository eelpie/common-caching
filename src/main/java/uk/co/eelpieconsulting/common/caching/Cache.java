package uk.co.eelpieconsulting.common.caching;

public interface Cache {

	public void put(String key, int ttl, Object value);
	public Object get(String key);
	public void decache(String key);

}