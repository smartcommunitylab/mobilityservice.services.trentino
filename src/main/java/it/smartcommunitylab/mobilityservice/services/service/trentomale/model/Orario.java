package it.smartcommunitylab.mobilityservice.services.service.trentomale.model;

public class Orario implements Comparable<Orario> {

	private Integer c_train;
    private Integer c_stop;
    private String i_minutes;
    private String i_hours;
    
	public Integer getC_train() {
		return c_train;
	}
	public void setC_train(Integer c_train) {
		this.c_train = c_train;
	}
	public Integer getC_stop() {
		return c_stop;
	}
	public void setC_stop(Integer c_stop) {
		this.c_stop = c_stop;
	}
	public String getI_minutes() {
		return i_minutes;
	}
	public void setI_minutes(String i_minutes) {
		this.i_minutes = i_minutes;
	}
	public String getI_hours() {
		return i_hours;
	}
	public void setI_hours(String i_hours) {
		this.i_hours = i_hours;
	}
	
	public int compareTo(Orario o) {
		return (i_hours + ":" + i_minutes).compareTo((o.i_hours + ":" + o.i_minutes));
	}
	
    
    
}
