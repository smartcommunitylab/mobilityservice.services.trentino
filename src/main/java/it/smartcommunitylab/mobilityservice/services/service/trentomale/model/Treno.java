package it.smartcommunitylab.mobilityservice.services.service.trentomale.model;

public class Treno {

    private Integer  currentDelay;
    private Integer position;
    private Integer trainId;
    private Integer trainNumber;
    
	public Integer getCurrentDelay() {
		return currentDelay;
	}
	public void setCurrentDelay(Integer currentDelay) {
		this.currentDelay = currentDelay;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public Integer getTrainId() {
		return trainId;
	}
	public void setTrainId(Integer trainId) {
		this.trainId = trainId;
	}
	public Integer getTrainNumber() {
		return trainNumber;
	}
	public void setTrainNumber(Integer trainNumber) {
		this.trainNumber = trainNumber;
	}
    
    
	
}
