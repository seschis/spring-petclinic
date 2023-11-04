package org.springframework.samples.petclinic.diagnostics;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.Map;

public class DiagnosticData {

	Map<String, String> properties;

	public DiagnosticData(Map<String, String> properties) {
		this.properties = properties;
	}

	@JsonAnyGetter
	public Map<String, String> getProperties() {
		return properties;
	}

}
