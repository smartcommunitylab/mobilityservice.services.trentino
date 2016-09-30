package it.smartcommunitylab.mobilityservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MobilityServicesStorage {

	private static final String SERVICE_DATA = "serviceData";
	
	@Autowired
	@Qualifier("domainMongoTemplate")
	MongoTemplate template;	
	
	public MobilityServicesStorage() {
	}
	
	public void reset(Class clz) {
		template.dropCollection(clz);
	}
	
	public void save(MobilityServiceObjectsContainer container, boolean updateTimestamp) {
		if (updateTimestamp) {
			container.setTimestamp(System.currentTimeMillis());
		}
		template.save(container, SERVICE_DATA);
	}	
	
	public MobilityServiceObjectsContainer load(MobilityService service) {
		Criteria criteria = new Criteria("_id").is(service.generateId());
		List<MobilityServiceObjectsContainer> containers = template.find(new Query(criteria), MobilityServiceObjectsContainer.class, SERVICE_DATA);
		if (!containers.isEmpty()) {
			return containers.get(0);
		}
		
		return null;
	}		
	
//	public void save(List<MobilityServiceObject> data) {
//		for (MobilityServiceObject o: data) {
//			o.setTimestamp(System.currentTimeMillis());
//			template.save(o);
//		}
//	}
	
	public List<MobilityServiceObject> load(Class clz, Query query) {
		if (query != null) {
			return template.find(query, clz);
		} else {
			return template.findAll(clz);
		}
	}
	
}
