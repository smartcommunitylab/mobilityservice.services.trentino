package it.smartcommunitylab.mobilityservice.services.test;

import it.smartcommunitylab.mobilityservice.services.MobilityService;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceException;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceObjectsContainer;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class ServiceTest extends MobilityService {

	private int iterations;
	
	@Override
	protected synchronized List<MobilityServiceObject> invokeService() throws MobilityServiceException {
		List<MobilityServiceObject> result = Lists.newArrayList();

		iterations++;
		
		System.out.println("I=" + iterations);
		
		if (iterations % 3 == 2) {
			System.out.println("Fail");
			throw new MobilityServiceException("Failed at " + iterations);
		}
		
		return result;
	}

	public int getIterations() {
		return iterations;
	}

	@Override
	protected int publishData(MobilityServiceObjectsContainer data) throws MobilityServiceException {
		return 200;
	}

}
