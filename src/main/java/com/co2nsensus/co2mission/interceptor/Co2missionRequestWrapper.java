package com.co2nsensus.co2mission.interceptor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Co2missionRequestWrapper extends HttpServletRequestWrapper {

	private final String body;

	public Co2missionRequestWrapper(HttpServletRequest request) {
		super(request);

		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();

			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

				char[] charBuffer = new char[2048];
				int bytesRead = -1;

				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			log.error("Error reading the request body...");
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					log.error("Error closing bufferedReader...");
				}
			}
		}

		body = stringBuilder.toString();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());

		ServletInputStream inputStream = new ServletInputStream() {
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}

			@Override
			public boolean isFinished() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isReady() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setReadListener(ReadListener listener) {
				// TODO Auto-generated method stub

			}
		};

		return inputStream;
	}

	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new StringReader(body));
	}

	public String getBody() {
		return new String(body);
	}
}