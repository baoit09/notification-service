package com.fpt.poc.analytic.config;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.ge.predix.solsvc.timeseries.bootstrap.config.DefaultTimeseriesConfig;

@Component("defaultAnalyticsConfig")
@Scope("prototype")
public class DefaultAnlyticsConfig extends DefaultWebSocketConfigForAnalytics
		implements EnvironmentAware, IAnalyticConfig {

	private static Logger log = LoggerFactory.getLogger(DefaultTimeseriesConfig.class);

	@Value("${predix.analytics.catalogUrl}")
	private String catalogUrl;

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
		vcapPropertyName = "vcap.services." + tsName + ".credentials.query.uri";
		if (!StringUtils.isEmpty(env.getProperty(vcapPropertyName))) {
			this.catalogUrl = env.getProperty(vcapPropertyName);
		}
		log.info("DefaultTimeseriesConfig=" + this.toString());

	}

	@Override
	public String getCatalogUrl() {
		// TODO Auto-generated method stub
		return this.catalogUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return "DefaultAnalyticsConfig [catalogUrl=" + this.catalogUrl + "] " + super.toString();
	}
}
