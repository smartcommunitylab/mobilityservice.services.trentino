package it.smartcommunitylab.mobilityservice.services.service.ordinanzerovereto.model;

import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;

import java.util.List;

import org.springframework.data.annotation.Id;

public class Ordinanza extends MobilityServiceObject {

	@Id
	private String id;
	private String oggetto;
	private String data;
	private String dal;
	private String al;
	private String tipologia;
	private String stato;
	private List<Via> vie;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String ogetto) {
		this.oggetto = ogetto;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDal() {
		return dal;
	}

	public void setDal(String dal) {
		this.dal = dal;
	}

	public String getAl() {
		return al;
	}

	public void setAl(String al) {
		this.al = al;
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public List<Via> getVie() {
		return vie;
	}

	public void setVie(List<Via> vie) {
		this.vie = vie;
	}

}
