package it.smartcommunitylab.mobilityservice.services;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.google.common.collect.Maps;

@Component
public class MobilityServicesManager {

	private static final String INFO = "info";

	private static final String PARAMETERS = "parameters";
	private static final String REFRESH = "refresh";
	private static final String ALIAS = "alias";

	private static final String ENABLED = "enabled";

	private static final String CLASS = "class";

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("classpath:/services-info.yml")
	private Resource resource;

	@Autowired
	private ThreadPoolTaskScheduler scheduler;

	private Map<String, MobilityService> servicesAliases;
	private List<Map<String, Object>> servicesInstances;

	private Map<MobilityService, ScheduledFuture<?>> scheduledFutures;

	@PostConstruct
	public void init() throws Exception {
		logger.info("Mobility services starting.");
		servicesAliases = Maps.newTreeMap();
		scheduledFutures = Maps.newHashMap();
		Yaml yaml = new Yaml(new Constructor(MobilityServicesManager.class));
		MobilityServicesManager data = (MobilityServicesManager) yaml.load(resource.getInputStream());
		this.servicesInstances = data.servicesInstances;

		for (Map<String, Object> map : servicesInstances) {
			Class c = Class.forName((String) map.get(CLASS));
			MobilityService service = (MobilityService) c.newInstance();
			service.setRefresh((String) map.get(REFRESH));
			service.setServiceId(c.getSimpleName());
			if (map.containsKey(PARAMETERS)) {
				service.setParameters((Map<String, Object>) map.get(PARAMETERS));
			}
			if (map.containsKey(INFO)) {
				service.setInfo((Map<String, Object>) map.get(INFO));
			}
			if (map.containsKey(ENABLED)) {
				service.setEnabled((Boolean) map.get(ENABLED));
			}
			if (map.containsKey(ALIAS)) {
				service.setAlias((String) map.get(ALIAS));
				servicesAliases.put(service.getAlias(), service);
			} else {
				logger.warn("Warning, missing alias for service " + service);
			}		
			if (service.getEnabled() == null || service.getEnabled().booleanValue()) {
				schedule(service);
				logger.info("Scheduled service " + service);
			} else {
				logger.info("Not scheduling disabled service " + service);
			}
		}
		logger.info("Mobility services started.");
	}
	
	public void shutdown() {
		for (ScheduledFuture future: scheduledFutures.values()) {
			future.cancel(false);
		}
		scheduler.shutdown();
	}

	public Map<String, MobilityService> getServicesAliases() {
		return servicesAliases;
	}

	public void setServicesAliases(Map<String, MobilityService> servicesAliases) {
		this.servicesAliases = servicesAliases;
	}	
	
	public List<Map<String, Object>> getServicesInstances() {
		return servicesInstances;
	}

	public void setServicesInstances(List<Map<String, Object>> servicesInstances) {
		this.servicesInstances = servicesInstances;
	}

	public MobilityServiceObjectsContainer getData(MobilityService service) {
		MobilityServiceObjectsContainer container = null;
		try {
			logger.info("Invoking service " + service + ".");
			List<MobilityServiceObject> result = service.invokeService();
			container = new MobilityServiceObjectsContainer(service);
			container.setObjects(result);

			logger.info("Invoked service " + service + ".");
			int res = service.publishData(container);
			if (res != 200) {
				logger.error("Mobilityservice returned " + res + " for service " + service + ".");	
			} else {
				logger.debug("Published data for service " + service + ".");
			}
		} catch (Exception e) {
			logger.info("Service " + service + " failed.");
			e.printStackTrace();
		}

		return container;
	}

	private void schedule(MobilityService service) {
		String schedule = service.getRefresh();
		Trigger trigger = buildTrigger(schedule);
		MobilityServiceTask task = new MobilityServiceTask(this, service);
		ScheduledFuture<MobilityService> future = (ScheduledFuture<MobilityService>) scheduler.schedule(task, trigger);
		scheduledFutures.put(service, future);
		logger.info("Scheduling service " + service + ": " + schedule);

	}
	
	private Trigger buildTrigger(String refresh) {
		try {
			long ref = Long.parseLong(refresh);
			return new PeriodicTrigger(ref);
		} catch (NumberFormatException e) {
			return new CronTrigger(refresh);
		}
	}	
	

}
