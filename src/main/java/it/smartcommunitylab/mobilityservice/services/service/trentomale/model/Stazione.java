package it.smartcommunitylab.mobilityservice.services.service.trentomale.model;

public class Stazione {

	private int position;
	private int id;
	private String name;
	
	
	public Stazione(int position, int id, String name) {
		this.position = position;
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	@Override
	public String toString() {
		return name;
	}
	
}
