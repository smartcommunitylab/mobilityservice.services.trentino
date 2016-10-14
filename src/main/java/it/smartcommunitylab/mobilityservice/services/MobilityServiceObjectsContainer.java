package it.smartcommunitylab.mobilityservice.services;

import java.util.List;
import java.util.Map;

public class MobilityServiceObjectsContainer {

	private String id;
	
	private String serviceId;
	private Map<String, Object> parameters;
	private Map<String, Object> info;
	private List<MobilityServiceObject> objects;
	private long timestamp;
	
	public MobilityServiceObjectsContainer() {
	}
	
	public MobilityServiceObjectsContainer(MobilityService service) {
		serviceId = service.getClass().getSimpleName();
		id = service.generateId();
		parameters = service.getParameters();
		info = service.getInfo();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String className) {
		this.serviceId = className;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public Map<String, Object> getInfo() {
		return info;
	}

	public void setInfo(Map<String, Object> info) {
		this.info = info;
	}

	public List<MobilityServiceObject> getObjects() {
		return objects;
	}

	public void setObjects(List<MobilityServiceObject> objects) {
		this.objects = objects;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
}
