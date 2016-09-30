package it.smartcommunitylab.mobilityservice.services.service.trentomale.model;

import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;

import org.springframework.data.annotation.Id;

public class TrentoMaleTrain extends MobilityServiceObject {

	@Id
	private int id;
	private int number;
	private String time;
	private String direction;
	private int delay;
	private String station;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

}
