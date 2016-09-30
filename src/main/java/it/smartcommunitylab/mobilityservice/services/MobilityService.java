package it.smartcommunitylab.mobilityservice.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public abstract class MobilityService {

	protected String serviceId;
	
	protected Map<String, Object> parameters;
	
	protected Map<String, Object> info;
	
	protected String cronRefresh;
	
	protected String failCronRefresh;
	
	protected Boolean enabled;

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

	public String getCronRefresh() {
		return cronRefresh;
	}

	public void setCronRefresh(String cronRefresh) {
		this.cronRefresh = cronRefresh;
	}
	
	public String getFailCronRefresh() {
		return failCronRefresh;
	}

	public void setFailCronRefresh(String failCronRefresh) {
		this.failCronRefresh = failCronRefresh;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String generateId() {
		return this.getClass().getSimpleName() + (parameters != null ? ("_" + Integer.toHexString(parameters.hashCode())) : "");
	}

		abstract protected List<MobilityServiceObject> invokeService() throws MobilityServiceException;

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + parameters;
	}
}
