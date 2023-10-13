package org.springframework.samples.petclinic.owner;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PhotoResponse(
	Integer page,
	@JsonProperty("per_page")
	Integer perPage,
	List<PhotoResource> photos,
	@JsonProperty("total_results")
	Integer totalResults,
	@JsonProperty("next_page")
	String nextPage
) {
}
