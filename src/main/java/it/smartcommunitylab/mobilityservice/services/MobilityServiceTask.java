package it.smartcommunitylab.mobilityservice.services;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.smartcommunitylab.mobilityservice.services.util.TrainsConverter;

public class MobilityServiceTask implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(TrainsConverter.class);

	private MobilityServicesManager manager;
	private MobilityService service;
	private volatile boolean running = false;
	
	public MobilityServiceTask(MobilityServicesManager manager, MobilityService service) {
		super();
		this.manager = manager;
		this.service = service;
	}

	public void run() {
		running = true;
		final Future<Void> handler = manager.getScheduler().submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				manager.getData(service);
				running = false;
				return null;
			}
		});
		try {
			Thread.sleep(60000);
			if (running) {
		    	 logger.error("Cancelling service " + service);
				handler.cancel(true);
			}
		} catch (Exception e) {}
		running = false;
//		manager.getScheduler().execute(new Runnable(){
//		     public void run(){
//		    	 logger.error("Cancelling service " + service);
//		         handler.cancel(true);
//		     }      
//		 }, 60000);		
	}

}
