package com.fpt.poc.microservice.notification.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fpt.poc.microservice.constants.Constants;
import com.fpt.poc.microservice.notification.api.INotificationAPI;
import com.fpt.poc.microservice.notification.dto.Device;
import com.fpt.poc.microservice.notification.dto.DeviceRequestWrapper;
import com.fpt.poc.microservice.notification.dto.PushRequestWrapper;
import com.ge.predix.solsvc.spi.IServiceManagerService;

@Component
public class NotificationImpl implements INotificationAPI {
	private Logger log = LoggerFactory.getLogger(NotificationImpl.class);
	private HttpURLConnection connection = null;
	private ArrayList<Device> devices = new ArrayList<Device>();

	@Autowired
	private IServiceManagerService serviceManagerService;

	/**
	 * -
	 */
	public NotificationImpl() {
		super();
	}

	/**
	 * -
	 */
	@SuppressWarnings("nls")
	@PostConstruct
	public void init() {
		try {
			this.serviceManagerService.createRestWebService(this, null);
		} catch (Exception e) {
			throw new RuntimeException("unable to set up notification=", e);
		}
	}

	@SuppressWarnings("nls")
	@Override
	public Response GetAllDevice() {
		return handleResult(devices);
	}

	@SuppressWarnings("nls")
	@Override
	public Response Delete(String id) {
		String result = "{ \"result\": \"success\"}";
		Device device = getDeviceByDeviceId(id);
		if (device == null) {
			result = "{ \"result\": \"Not found device\"}";
		}

		this.devices.remove(device);
		return handleResult(result);
	}

	@SuppressWarnings("nls")
	@Override
	public Response PushFollowDevice(String id, PushRequestWrapper requestWrapper) {
		try {
			Device device = getDeviceByDeviceId(id);
			if (device == null) {
				String result = "{ \"result\": \"Not found device\"}";
				return handleResult(result);
			}

			String requestBody = "{ \"notification\": {\n    \"title\": \"Health Care Application\",\n    \"body\": \""
					+ requestWrapper.message + "\"\n  },\n  \"to\" : \"" + device.RefreshToken + "\"\n}";

			String contentLength = Integer.toString(requestBody.getBytes().length);

			URL url = new URL(Constants.FirebaseUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "key=" + Constants.ServiceKey);
			connection.setRequestProperty("Content-Length", contentLength);
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(requestBody);
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder builder = new StringBuilder(); // or StringBuffer if
															// Java version 5+
			String line;
			while ((line = rd.readLine()) != null) {
				builder.append(line);
				builder.append('\r');
			}
			rd.close();
			String response = builder.toString();
			return handleResult(response);
		} catch (Throwable e) {
			log.error("unable to push", e);

			throw new RuntimeException("unable to push, errorMsg=" + e.getMessage(), e);
		}
	}

	@SuppressWarnings("nls")
	@Override
	public Response PushAll(PushRequestWrapper requestWrapper) {
		if (requestWrapper.message != "") {
			for (int i = 0; i < devices.size(); i++) {
				pushDevice(devices.get(i).RefreshToken, requestWrapper.message);
			}
		}

		String result = "{ \"result\": \"success\"}";
		return handleResult(result);
	}

	@SuppressWarnings("nls")
	@Override
	public Response Register(DeviceRequestWrapper requestWrapper) {
		try {
			boolean isExisted = false;
			Device temp = new Device(requestWrapper);
			if (validateRegisterRequest(requestWrapper)) {
				for (int i = 0; i < devices.size(); i++) {
					Device device = devices.get(i);
					if (temp.IsEqual(device)) {
						isExisted = true;
						break;
					}
				}

				if (!isExisted) {
					devices.add(temp);
				}
			}

			String result = "{ \"result\": \"success\"}";
			if (isExisted) {
				result = "{ \"result\": \"Existed device\"}";
			}

			return handleResult(result);
		} catch (Throwable e) {
			log.error("unable to push", e);

			throw new RuntimeException("unable to push, errorMsg=" + e.getMessage(), e);
		}
	}

	@SuppressWarnings("javadoc")
	protected Response handleResult(Object entity) {
		ResponseBuilder responseBuilder = Response.status(Status.OK);
		responseBuilder.type(MediaType.APPLICATION_JSON);
		responseBuilder.entity(entity);
		return responseBuilder.build();
	}

	private String pushDevice(String refreshToken, String message) {
		try {
			String requestBody = "{ \"notification\": {\n    \"title\": \"Health Care Application\",\n    \"body\": \""
					+ message + "\"\n  },\n  \"to\" : \"" + refreshToken + "\"\n}";

			String contentLength = Integer.toString(requestBody.getBytes().length);

			URL url = new URL(Constants.FirebaseUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "key=" + Constants.ServiceKey);
			connection.setRequestProperty("Content-Length", contentLength);
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(requestBody);
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder builder = new StringBuilder(); // or StringBuffer if
															// Java version 5+
			String line;
			while ((line = rd.readLine()) != null) {
				builder.append(line);
				builder.append('\r');
			}
			rd.close();
			String response = builder.toString();
			return response;
		} catch (Throwable e) {
			log.error("unable to push", e);

			throw new RuntimeException("unable to push, errorMsg=" + e.getMessage(), e);
		}
	}

	private boolean validateRegisterRequest(DeviceRequestWrapper deviceRequest) {
		if (deviceRequest.device == "" || deviceRequest.token == "") {
			return false;
		}
		return true;
	}

	private Device getDeviceByDeviceId(String deviceId) {
		for (int i = 0; i < devices.size(); i++) {
			Device device = devices.get(i);
			if (device.Device.equals(deviceId)) {
				return device;
			}
		}

		return null;
	}
}
