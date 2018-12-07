package com.msl.clientcertauth.gateway;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//@EnableConfigurationProperties(UriConfiguration.class)
//@RestController
public class SecureServerApplication {
	
	@Value( "${server.ssl.trust-store}" )
	private Resource trustStoreFile;
	
	@Value( "${server.ssl.trust-store-password}" )
	private String trustStorePassword;

	@PostConstruct
	public void initSsl() throws IOException{
		System.setProperty("javax.net.debug", "ssl");

//		System.setProperty("javax.net.ssl.trustStore", trustStoreFile.getURL().toString());
//		System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
		
		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
			(hostname,sslSession) -> {
				if (hostname.equals("localhost") || hostname.startsWith("es1pocmom01v")) {
					return true;
				}
				return false;
			});
	}

	public static void main(String[] args) {
		SpringApplication.run(SecureServerApplication.class, args);
	}
	
    @Bean
    RestTemplate restTemplate() throws Exception {
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(
                		trustStoreFile.getURL(),
                        trustStorePassword.toCharArray()
                ).build();
        // TODO: Remove when we have a CA Signed certificate in the backend
        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
        
        SSLConnectionSocketFactory socketFactory = 
                new SSLConnectionSocketFactory(sslContext, allowAllHosts);
        HttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory).build();
        HttpComponentsClientHttpRequestFactory factory = 
                new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }

//	@Bean
//	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
//		String httpUri = "https://es1pocmom01v:8243";
//		return builder.routes()
//				.route(p -> p.path("/*").filters(f -> f.addRequestHeader("Authorization", "Bearer: ")).uri(httpUri))
//				.build();
//	}


//}
//
//@ConfigurationProperties
//class UriConfiguration {
//
//	private String httpAPIM = "https://es1pocmom01v:8243";
//
//	public String getHttpAPIM() {
//		return httpAPIM;
//	}
//
//	public void setHttpAPIM(String httpAPIM) {
//		this.httpAPIM = httpAPIM;
//	}
}
