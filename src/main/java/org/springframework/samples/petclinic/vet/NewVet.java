package org.springframework.samples.petclinic.vet;

import java.io.Serializable;

public class NewVet implements Serializable {

	private String firstName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

}
