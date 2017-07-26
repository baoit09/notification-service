package com.fpt.poc.microservice.notification.dto;

public class Device {
	public String Device;
	public String Tag;
	public String RefreshToken;

	public Device() {
	}

	public Device(DeviceRequestWrapper wrapper) {
		this.Device = wrapper.device;
		this.Tag = wrapper.tag;
		this.RefreshToken = wrapper.token;
	}

	public boolean IsEqual(Device device) {		
		return Device.equals(device.Device) && Tag.equals(device.Tag);
	}
}
