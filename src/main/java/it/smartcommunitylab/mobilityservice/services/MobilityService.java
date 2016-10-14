package it.smartcommunitylab.mobilityservice.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public abstract class MobilityService {

	protected String serviceId;
	
	protected Map<String, Object> parameters;
	
	protected Map<String, Object> info;
	
	protected String refresh;

	protected Boolean enabled = true;

	protected String alias;	
	
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	public String getRefresh() {
		return refresh;
	}

	public void setRefresh(String refresh) {
		this.refresh = refresh;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String generateId() {
		return this.getClass().getSimpleName() + (parameters != null ? ("_" + Integer.toHexString(parameters.hashCode())) : "");
	}

	abstract protected List<MobilityServiceObject> invokeService() throws MobilityServiceException;
	
	abstract protected int publishData(MobilityServiceObjectsContainer data) throws MobilityServiceException;

	@Override
	public String toString() {
		return "[" + (alias != null ? alias : (serviceId + ": " + parameters)) + "]";
	}

}
