package it.smartcommunitylab.mobilityservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;


public class MobilityServiceObject {

//	private long timestamp;
//	
//	public long getTimestamp() {
//		return timestamp;
//	}
//
//	public void setTimestamp(long timestamp) {
//		this.timestamp = timestamp;
//	}
	
	@Override
	public String toString() {
//		return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
		try {
			ObjectMapper mapper = new ObjectMapper();
//			return mapper.defaultPrettyPrintingWriter().writeValueAsString(this);
			return mapper.writeValueAsString(this);
		} catch (Exception e) {
			return null;
		}
	}	
	
	
}
