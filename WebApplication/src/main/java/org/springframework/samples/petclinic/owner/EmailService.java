package org.springframework.samples.petclinic.owner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EmailService {

	@Value("${email.service.address}")
	private String serviceAddress;

	public void sendEmail(Owner owner) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.postForEntity(serviceAddress, owner, String.class);
		}
		catch (Exception e) {
			// do nothing.
		}
	}

}
