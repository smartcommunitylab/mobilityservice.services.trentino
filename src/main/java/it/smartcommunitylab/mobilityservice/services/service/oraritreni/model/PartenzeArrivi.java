package it.smartcommunitylab.mobilityservice.services.service.oraritreni.model;

import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;

import org.springframework.data.annotation.Id;

public class  PartenzeArrivi  extends MobilityServiceObject {

	@Id
	private String stazione;
	private Partenze part;
	private Arrivi arr;

	public String getStazione() {
		return stazione;
	}

	public void setStazione(String stazione) {
		this.stazione = stazione;
	}

	public Partenze getPart() {
		return part;
	}

	public void setPart(Partenze part) {
		this.part = part;
	}

	public Arrivi getArr() {
		return arr;
	}

	public void setArr(Arrivi arr) {
		this.arr = arr;
	}

}
