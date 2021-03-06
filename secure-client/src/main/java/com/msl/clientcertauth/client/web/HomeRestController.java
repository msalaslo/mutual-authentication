package com.msl.clientcertauth.client.web;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class HomeRestController {

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/")
	public String home() throws RestClientException, URISyntaxException {
//		return restTemplate.getForObject(new URI("https://localhost:8443"), String.class);
		return restTemplate.getForObject(new URI("https://localhost:8443/config/v1.0/installation/cu"), String.class);
//		return restTemplate.getForObject(new URI("https://es1pocmom01v:8443"), String.class);
	}

}
