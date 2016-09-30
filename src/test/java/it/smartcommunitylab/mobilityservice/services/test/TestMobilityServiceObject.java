package it.smartcommunitylab.mobilityservice.services.test;

import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;

import org.springframework.data.annotation.Id;

public class TestMobilityServiceObject extends MobilityServiceObject {

	@Id
	private int id;
	
	private boolean result;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
	
	
	
}
