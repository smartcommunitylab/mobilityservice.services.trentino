package it.smartcommunitylab.mobilityservice.services.service.parcheggi.model;

import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;

public class Parcheggio extends MobilityServiceObject {

	private String id;
	private String address;
	private String places;

	// optional it.sayservice.platform.core.message.POI poi = 4;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPlaces() {
		return places;
	}

	public void setPlaces(String places) {
		this.places = places;
	}



}
