package it.smartcommunitylab.mobilityservice.services.service.ordinanzerovereto.model;

public class Via {

	private String codiceVia;
	private String descrizioneVia;
	private String note;
	private double lat;
	private double lng;
	private String dalCivico;
	private String alCivico;
	private String dalIntersezione;
	private String alIntersezione;
	private String tipologia;
	public String getCodiceVia() {
		return codiceVia;
	}
	public void setCodiceVia(String codice_via) {
		this.codiceVia = codice_via;
	}
	public String getDescrizioneVia() {
		return descrizioneVia;
	}
	public void setDescrizioneVia(String descrizione_via) {
		this.descrizioneVia = descrizione_via;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getDalCivico() {
		return dalCivico;
	}
	public void setDalCivico(String dal_civico) {
		this.dalCivico = dal_civico;
	}
	public String getAlCivico() {
		return alCivico;
	}
	public void setAlCivico(String al_civico) {
		this.alCivico = al_civico;
	}
	public String getDalIntersezione() {
		return dalIntersezione;
	}
	public void setDalIntersezione(String dal_intersezione) {
		this.dalIntersezione = dal_intersezione;
	}
	public String getAlIntersezione() {
		return alIntersezione;
	}
	public void setAlIntersezione(String al_intersezione) {
		this.alIntersezione = al_intersezione;
	}
	public String getTipologia() {
		return tipologia;
	}
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	
}
