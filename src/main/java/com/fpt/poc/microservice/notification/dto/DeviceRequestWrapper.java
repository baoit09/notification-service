package com.fpt.poc.microservice.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceRequestWrapper {
	@JsonProperty("device")
	public String device;
	
	@JsonProperty("token")
	public String token;
}
