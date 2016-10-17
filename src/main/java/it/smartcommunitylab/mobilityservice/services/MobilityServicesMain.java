package it.smartcommunitylab.mobilityservice.services;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MobilityServicesMain {

	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(MobilityServicesConfig.class);
		context.refresh();
		
		MobilityServicesManager manager = context.getBean(MobilityServicesManager.class);
	}

}
