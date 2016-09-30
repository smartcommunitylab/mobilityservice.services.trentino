package it.smartcommunitylab.mobilityservice.services;


public class MobilityServiceTask implements Runnable {

	private MobilityServicesManager manager;
	private MobilityServiceResultProcessor handler;
	private MobilityService service;

	public MobilityServiceTask(MobilityServicesManager manager, MobilityService service, MobilityServiceResultProcessor handler) {
		super();
		this.manager = manager;
		this.handler = handler;
		this.service = service;
	}

	public void run() {
		MobilityServiceObjectsContainer result = manager.getData(service);
		if (handler != null) {
			try {
				handler.process(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
