package it.smartcommunitylab.mobilityservice.services.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class TestScheduling {

	@org.junit.Test
	public void test() throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(MobilityServicesTestConfig.class);
		context.refresh();
		
		Thread.sleep(1000 * 60 * 3);
	}
	
}
