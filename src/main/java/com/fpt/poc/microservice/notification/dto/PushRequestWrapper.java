package com.fpt.poc.microservice.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PushRequestWrapper {
	@JsonProperty("message")
	public String message;
}
