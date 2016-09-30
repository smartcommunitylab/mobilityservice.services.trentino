package it.smartcommunitylab.mobilityservice.services;

public class MobilityServiceException extends Exception {

	public MobilityServiceException() {
		super();
	}

	public MobilityServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MobilityServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public MobilityServiceException(String message) {
		super(message);
	}

	public MobilityServiceException(Throwable cause) {
		super(cause);
	}

}
