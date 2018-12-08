package com.msl.clientcertauth.gateway.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class HomeRestController {
	
	@Value( "${http.client.backend.protocol}" )
	private String backendProtocol;
	
	@Value( "${http.client.backend.host}" )
	private String backendHost;
	
	@Value( "${http.client.backend.port}" )
	private String backendPort;
	
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/")
	public String home(Principal principal) {
		return String.format("Hello %s!", principal.getName());
	}

	@RequestMapping(value = "/config/**")
	public String gateway(HttpServletRequest request) throws RestClientException, URISyntaxException{
		String contextPath = request.getRequestURI();
		String response = restTemplate.getForObject(new URI(backendProtocol + "://" + backendHost + ":" + backendPort + contextPath), String.class);
		return response;
	}
}
