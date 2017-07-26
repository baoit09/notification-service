package com.fpt.poc.analytic.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.ge.predix.solsvc.restclient.config.DefaultOauthRestConfig;
import com.ge.predix.solsvc.websocket.config.IWebSocketConfig;

@Component("defaultAnalyticsConfig")
public class DefaultWebSocketConfigForAnalytics extends DefaultOauthRestConfig
		implements IWebSocketConfig, EnvironmentAware {

	@Value("${predix.analytics.websocket.uri}")
	private String wsUri;

	@Value("${predix.analytics.zoneid.header:Predix-Zone-Id}")
	private String zoneIdHeader;

	@Value("${predix.analytics.zoneid:#{null}}")
	private String zoneId;

	@Value("${predix.analytics.websocket.pool.maxIdle:5}")
	private int wsMaxIdle;

	@Value("${predix.analytics.websocket.pool.maxActive:5}")
	private int wsMaxActive;

	@Value("${predix.analytics.websocket.pool.maxWait:8000}")
	private int wsMaxWait;

	/**
	 * The name of the VCAP property holding the name of the bound analytics end
	 * point
	 */
	public static final String ANALYTICS_VCAPS_NAME = "predix_analytics_name"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ge.predix.solsvc.websocket.config.IWebSocketConfig#getWsUri()
	 */
	@Override
	public String getWsUri() {
		return this.wsUri;
	}

	/**
	 * you may override the setter with an @value annotated property
	 * 
	 * @param wsUri
	 *            -
	 */
	public void setWsUri(String wsUri) {
		this.wsUri = wsUri;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ge.predix.solsvc.timeseries.bootstrap.config.ITimeseriesConfig#
	 * getPredixZoneIdHeaderName()
	 */
	@Override
	public String getZoneIdHeader() {
		return this.zoneIdHeader;
	}

	/**
	 * you may override the setter with an @value annotated property
	 * 
	 * @param zoneId
	 *            -
	 */
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ge.predix.solsvc.websocket.config.IWebSocketConfig#getZoneId()
	 */
	@Override
	public String getZoneId() {
		return this.zoneId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ge.predix.solsvc.websocket.config.IWebSocketConfig#getWsMaxIdle()
	 */
	@Override
	public int getWsMaxIdle() {
		return this.wsMaxIdle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ge.predix.solsvc.websocket.config.IWebSocketConfig#getWsMaxActive()
	 */
	@Override
	public int getWsMaxActive() {
		return this.wsMaxActive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ge.predix.solsvc.websocket.config.IWebSocketConfig#getWsMaxWait()
	 */
	@Override
	public int getWsMaxWait() {
		return this.wsMaxWait;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.EnvironmentAware#setEnvironment(org.
	 * springframework.core.env.Environment)
	 */
	@SuppressWarnings("nls")
	@Override
	public void setEnvironment(Environment env) {
		super.setEnvironment(env);
		String vcapPropertyName = null;
		String tsName = env.getProperty(ANALYTICS_VCAPS_NAME); // this is set
																// on the
																// manifest
																// of the
																// application

		vcapPropertyName = null;
		vcapPropertyName = "vcap.services." + tsName + ".credentials.ingest.uri";
		if (!StringUtils.isEmpty(env.getProperty(vcapPropertyName))) {
			this.wsUri = env.getProperty(vcapPropertyName);
		}

		vcapPropertyName = "vcap.services." + tsName + ".credentials.query.zone-http-header-name";
		if (!StringUtils.isEmpty(env.getProperty(vcapPropertyName))) {
			this.zoneIdHeader = env.getProperty(vcapPropertyName);
		}

		vcapPropertyName = "vcap.services." + tsName + ".credentials.ingest.zone-http-header-value";
		if (!StringUtils.isEmpty(env.getProperty(vcapPropertyName))) {
			this.zoneId = env.getProperty(vcapPropertyName);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return "DefaultWebSocketConfigForAnalytics [wsUri=" + this.wsUri + ", zoneIdHeader=" + this.zoneIdHeader
				+ ", zoneId=" + this.zoneId + ", wsMaxIdle=" + this.wsMaxIdle + ", wsMaxActive=" + this.wsMaxActive
				+ ", wsMaxWait=" + this.wsMaxWait + "] " + super.toString();
	}
}
