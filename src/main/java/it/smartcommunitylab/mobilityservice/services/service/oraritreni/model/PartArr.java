package it.smartcommunitylab.mobilityservice.services.service.oraritreni.model;

public class PartArr {
	private String codtreno;
	private String fromOrTo;
	private String ora;
	private String binPrevisto;
	private String binReale;
	private String ritardo;

	public String getCodtreno() {
		return codtreno;
	}

	public void setCodtreno(String codtreno) {
		this.codtreno = codtreno;
	}

	public String getFromOrTo() {
		return fromOrTo;
	}

	public void setFromOrTo(String fromOrTo) {
		this.fromOrTo = fromOrTo;
	}

	public String getOra() {
		return ora;
	}

	public void setOra(String ora) {
		this.ora = ora;
	}

	public String getBinPrevisto() {
		return binPrevisto;
	}

	public void setBinPrevisto(String binPrevisto) {
		this.binPrevisto = binPrevisto;
	}

	public String getBinReale() {
		return binReale;
	}

	public void setBinReale(String binReale) {
		this.binReale = binReale;
	}

	public String getRitardo() {
		return ritardo;
	}

	public void setRitardo(String ritardo) {
		this.ritardo = ritardo;
	}

}
