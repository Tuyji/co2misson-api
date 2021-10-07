package com.co2nsensus.co2mission.model.dto.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Performance {

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");

	private long duration = 0;

	private long requestTimestamp;

	private long responseTimestamp;

	private String correlationID;

	public Performance(String correlationId) {
		this.requestTimestamp = System.currentTimeMillis();
		this.correlationID = correlationId;
	}

	public String getRequestTimestamp() {
		Date date = new Date(requestTimestamp);
		return formatter.format(date);
	}

	public String getResponseTimestamp() {
		Date date = new Date(responseTimestamp);
		return formatter.format(date);
	}

	public void setResponseTimestamp() {
		this.responseTimestamp = System.currentTimeMillis();
		this.duration = this.responseTimestamp - this.requestTimestamp;
	}

	public long getDuration() {
		return duration;
	}

	@Override
	public String toString() {
		return (new StringBuilder("Performance [duration=").append(duration).append(", requestTimestamp=")
				.append(getRequestTimestamp()).append(", responseTimestamp=").append(getResponseTimestamp())
				.append(", correlationID=").append(correlationID).append("]")).toString();
	}
}
