package it.smartcommunitylab.mobilityservice.services;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Component
public class ServicesCache {

	LoadingCache<Class, List<MobilityServiceObject>> cache;
	
	//todo autowired
	MobilityServicesStorage storage;
	
	public ServicesCache() throws UnknownHostException {
		storage = new MobilityServicesStorage();
		
		cache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.SECONDS).build(new CacheLoader<Class, List<MobilityServiceObject>>() {
			@Override
			public List<MobilityServiceObject> load(Class key) throws Exception {
				System.out.println("load");
				return storage.load(key, null);
			}});

	}
	
	public List<MobilityServiceObject> get(Class clz) throws ExecutionException {
		return cache.get(clz);
	}
	
}
