package it.smartcommunitylab.mobilityservice.services;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.google.common.collect.Maps;

@Component
public class MobilityServicesManager {

	private static final String INFO = "info";

	private static final String SERVICE_ID = "serviceId";
	private static final String PARAMETERS = "parameters";
	private static final String CRON_REFRESH = "cronRefresh";

	private static final String ENABLED = "enabled";

	private static final String CLASS = "class";

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("classpath:/services-info.yml")
	private Resource resource;

	@Autowired
	private MobilityServicesStorage storage;

	@Autowired
	private MobilityServiceNotifier notifier;

	@Autowired
	private ThreadPoolTaskScheduler scheduler;

	@Autowired(required = false)
	private MobilityServiceResultProcessor processor;

	private List<Map<String, Object>> servicesInstances;

	private Map<MobilityService, ScheduledFuture<?>> scheduledFutures;

	@PostConstruct
	public void init() throws Exception {
		logger.info("Mobility services starting.");
		scheduledFutures = Maps.newHashMap();
		Yaml yaml = new Yaml(new Constructor(MobilityServicesManager.class));
		MobilityServicesManager data = (MobilityServicesManager) yaml.load(resource.getInputStream());
		this.servicesInstances = data.servicesInstances;

		for (Map<String, Object> map : servicesInstances) {
			Class c = Class.forName((String) map.get(CLASS));
			MobilityService service = (MobilityService) c.newInstance();
			service.setCronRefresh((String) map.get(CRON_REFRESH));
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
			if (service.getEnabled() == null || service.getEnabled().booleanValue()) {
				MobilityServiceObjectsContainer old = storage.load(service);
				if (old != null) {
					old.setFails(0);
					storage.save(old, false);
				}
				schedule(service);
				logger.info("Scheduled service " + service.getServiceId());
			} else {
				logger.info("Not scheduling disabled service " + service.getServiceId());
			}
		}
		logger.info("Mobility services started.");
	}

	public List<Map<String, Object>> getServicesInstances() {
		return servicesInstances;
	}

	public void setServicesInstances(List<Map<String, Object>> servicesInstances) {
		this.servicesInstances = servicesInstances;
	}

	public synchronized MobilityServiceObjectsContainer getData(MobilityService service) {
		MobilityServiceObjectsContainer container = null;
		try {
			logger.info("Invoking service " + service.getClass().getSimpleName() + ".");
			List<MobilityServiceObject> result = service.invokeService();
			container = new MobilityServiceObjectsContainer(service);
			container.setObjects(result);
			container.setFails(0);

			MobilityServiceObjectsContainer old = storage.load(service);
			if (old != null && old.getFails() >= 2) {
				// restored
				String msg = "Service " + service.getClass().getSimpleName() + " resumed.";
				logger.warn(msg);
				notifier.sendServiceNotification(msg, "");
			}
			storage.save(container, true);
			logger.debug("Invoked service " + service.getClass().getSimpleName() + ".");
		} catch (Exception e) {
			container = storage.load(service);
			if (container != null) {
				logger.warn("Returning stored data for " + service.getClass().getSimpleName() + ".");
				if (container.getObjects() == null) {
					logger.warn("No valid stored data found for " + service.getClass().getSimpleName() + ".");
				}
				container.setFails(container.getFails() + 1);
				logger.error("Service " + service.getClass().getSimpleName() + " failed (times: " + container.getFails() + ").");
				storage.save(container, false);
			} else {
				container = new MobilityServiceObjectsContainer(service);
				container.setFails(1);
				storage.save(container, true);
				logger.warn("No stored data found for " + service.getClass().getSimpleName() + ".");
			}
			if (container.getFails() == 2) {
				// failed
				e.printStackTrace();
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				pw.flush();
				String msg = "Service " + service.getClass().getSimpleName() + " failed (times: " + container.getFails() + ").";
				logger.error(msg);
				notifier.sendServiceNotification(msg, sw.toString());
			}
		}

		return container;
	}

	private synchronized void schedule(MobilityService service) {
		String schedule = service.getCronRefresh();
		CronTrigger trigger = new CronTrigger(schedule);
		MobilityServiceTask task = new MobilityServiceTask(this, service, processor);
		ScheduledFuture<MobilityService> future = scheduler.schedule(task, trigger);
		scheduledFutures.put(service, future);
		logger.info("Scheduling service " + service.getServiceId() + ": " + schedule);

	}

}
