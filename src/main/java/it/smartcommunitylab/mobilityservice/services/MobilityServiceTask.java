package it.smartcommunitylab.mobilityservice.services;


public class MobilityServiceTask implements Runnable {

	private MobilityServicesManager manager;
	private MobilityService service;

	public MobilityServiceTask(MobilityServicesManager manager, MobilityService service) {
		super();
		this.manager = manager;
		this.service = service;
	}

	public void run() {
		MobilityServiceObjectsContainer result = manager.getData(service);
	}

}
