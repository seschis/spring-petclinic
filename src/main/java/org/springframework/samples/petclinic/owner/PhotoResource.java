package org.springframework.samples.petclinic.owner;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PhotoResource(
	Integer id,
	Integer width,
	Integer height,
	String url,
	String photographer,
	@JsonProperty("photographer_url")
	String photographerUrl,
	@JsonProperty("photographer_id")
	Integer photographerId,
	@JsonProperty("avg_color")
	String avgColor,
	PhotoSource src,
	Boolean liked,
	String alt
) {
}
