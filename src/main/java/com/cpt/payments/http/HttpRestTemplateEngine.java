package com.cpt.payments.http;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cpt.payments.util.LogMessage;

@Component
public class HttpRestTemplateEngine {
	private static final Logger LOGGER = LogManager.getLogger(HttpRestTemplateEngine.class);

	public ResponseEntity<String> execute(HttpRequest httpRequest) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			HttpEntity<?> request = new HttpEntity<>(httpRequest.getRequest(), headers);

			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			requestFactory.setOutputStreaming(false);
			restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(requestFactory));

			HttpMethod method = prepareHttpMethod(httpRequest.getHttpMethod());
			return restTemplate.exchange(httpRequest.getUrl(), method, request, String.class);
		} catch (Exception e) {
			LogMessage.logException(LOGGER, e);
			e.printStackTrace();
			return null;
		}
	}

	private HttpMethod prepareHttpMethod(HttpMethod methodType) {
		switch (methodType) {
		case POST:
			return HttpMethod.POST;
		case GET:
			return HttpMethod.GET;
		case PATCH:
			return HttpMethod.PATCH;
		case PUT:
			return HttpMethod.PUT;
		default:
			LogMessage.log(LOGGER, "default httpMethod POST ");
			return HttpMethod.POST;
		}
	}

}
