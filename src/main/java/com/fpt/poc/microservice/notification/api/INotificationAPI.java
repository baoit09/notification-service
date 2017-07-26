package com.fpt.poc.microservice.notification.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

import com.fpt.poc.microservice.notification.dto.DeviceRequestWrapper;
import com.fpt.poc.microservice.notification.dto.PushRequestWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author TamNT -
 */
@Consumes({ "application/json", "application/xml" })
@Produces({ "application/json", "application/xml" })
@Path("/notification")
@Api(value = "/notification")
public interface INotificationAPI {
	@POST
	@Path("/push/tag/{id}")
	@ApiOperation(value = "/push/tag/{id}")
	public Response PushFollowTag(@PathParam("id") String id, @RequestBody PushRequestWrapper requestWrapper);
	
	@POST
	@Path("/push/device/{id}")
	@ApiOperation(value = "/push/device/{id}")
	public Response PushFollowDevice(@PathParam("id") String id, @RequestBody PushRequestWrapper requestWrapper);

	@POST
	@Path("/push/all")
	@ApiOperation(value = "/push/all")
	Response PushAll(@RequestBody String requestWrapper);

	@POST
	@Path("/register")
	@ApiOperation(value = "/register")
	public Response Register(@RequestBody DeviceRequestWrapper requestWrapper);
}
