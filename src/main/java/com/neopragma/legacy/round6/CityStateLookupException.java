package com.neopragma.legacy.round6;

public class CityStateLookupException extends RuntimeException {
	
	public CityStateLookupException(Throwable cause) {
		super(cause);
	}
	
	public CityStateLookupException(String message) {
		super(message);
	}
	
	public CityStateLookupException(String message, Throwable cause) {
		super(message, cause);
	}
}
