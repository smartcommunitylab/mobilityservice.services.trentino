package it.smartcommunitylab.mobilityservice.services.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:mobility.properties")
public class ConnectionHelper {

//	private static final String PATH_TOKEN = "oauth/token";
//
//	public static final String MS_APP = "core.mobility";
//
//	@Value("${aacURL}")
//	private String aacURL;
//	@Value("${smartcampus.clientId}")
//	private String clientId = null;
//	@Value("${smartcampus.clientSecret}")
//	private String clientSecret = null;
//
//	private String token = null;
//	private Long expiresAt = null;
//	
	private static ConnectionHelper instance;
	
	@Value("${mobility.url}")
	private String mobilityURL;
	@Value("${services.user}")
	private String user;
	@Value("${services.password}")
	private String password;	

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static ConnectionHelper getInstance() {
		return instance;
	}
	
	public String getMobilityURL() {
		return mobilityURL;
	}

	public String getUser() {
		return user;
	}
	
	public String getPassword() {
		return password;
	}

//	public String getToken() {
//		try {
//		if (token == null || System.currentTimeMillis() + 10000 > expiresAt) {
//			if (!aacURL.endsWith("/"))
//				aacURL += "/";
//			String url = aacURL + PATH_TOKEN + "?grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret;
//			final HttpGet get = new HttpGet(url);
//
//			HttpResponse<Map> resp = Unirest.get(url).header("Accept", "application/json").asObject(Map.class);
//
//			if (resp.getStatus() == HttpStatus.SC_OK) {
//				Map map = resp.getBody();
//				expiresAt = System.currentTimeMillis() + (Integer) map.get("expires_in") * 1000;
//				token = (String) map.get("access_token");
//			}
//		}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return token;
//	}

}
