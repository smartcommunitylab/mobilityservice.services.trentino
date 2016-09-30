package it.smartcommunitylab.mobilityservice.services.service.tobike.model;

import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;

import org.springframework.data.annotation.Id;

public class TobikeStation extends MobilityServiceObject {

	@Id
	private String codice;
	private String nome;
	private String indirizzo;
	private double latitude;
	private double longitude;
	private String stato;
	private int posti;
	private int biciclette;
	private int stalli;

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public int getPosti() {
		return posti;
	}

	public void setPosti(int posti) {
		this.posti = posti;
	}

	public int getBiciclette() {
		return biciclette;
	}

	public void setBiciclette(int biciclette) {
		this.biciclette = biciclette;
	}

	public int getStalli() {
		return stalli;
	}

	public void setStalli(int stalli) {
		this.stalli = stalli;
	}

}
