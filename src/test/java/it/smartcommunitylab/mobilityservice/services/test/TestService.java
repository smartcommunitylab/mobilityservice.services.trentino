package it.smartcommunitylab.mobilityservice.services.test;

import it.smartcommunitylab.mobilityservice.services.MobilityService;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceException;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class TestService extends MobilityService {

	private int i;
	
	@Override
	protected List<MobilityServiceObject> invokeService() throws MobilityServiceException {
		List<MobilityServiceObject> result = Lists.newArrayList();

		i++;
		
		System.out.println("I=" + i);
		
		if (i % 3 != 0) {
			throw new MobilityServiceException("Failed at " + i);
		}
		
		return result;
	}

}
