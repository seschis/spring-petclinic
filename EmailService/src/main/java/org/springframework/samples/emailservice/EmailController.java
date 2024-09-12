package org.springframework.samples.emailservice;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

	Logger logger = LogManager.getLogger(EmailController.class);


	@PostMapping("/registerEmail")
	public String registerEmail(@RequestBody Owner owner) {
		logger.info("owner email : " +owner.getEmail() + " owner first name : "
			+owner.getFirstName()+" last name : " + owner.getLastName());
		return "";
	}
}
