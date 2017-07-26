package com.fpt.poc.analytic.config;

import com.ge.predix.solsvc.websocket.config.IWebSocketConfig;

public interface IAnalyticConfig extends IWebSocketConfig {
	/**
	 * @return -
	 */
	public abstract String getCatalogUrl();
}
