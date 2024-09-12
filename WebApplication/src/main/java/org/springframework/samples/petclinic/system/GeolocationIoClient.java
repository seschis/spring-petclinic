package org.springframework.samples.petclinic.system;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class GeolocationIoClient {

	private final String baseUrl;

	private final RestTemplate client;

	private final String apiKey = "8d6bc82c1a5d4d699477c530bb347055";

	public GeolocationIoClient(String baseUrl) {

		this.baseUrl = baseUrl;
		this.client = new RestTemplateBuilder().requestFactory(SimpleClientHttpRequestFactory.class).build();
	}

	public ResponseDto fetchAstronomyData(String location) {
		String urlTemplate = UriComponentsBuilder.fromHttpUrl(baseUrl + "/astronomy")
			.queryParam("apiKey", "{apiKey}")
			.queryParam("location", "{location}")
			.build()
			.toUriString();
		return client.getForObject(urlTemplate, ResponseDto.class,
				Map.of("apiKey", this.apiKey, "location", "New York, US"));
	}

	public static class LocationDto {

		private String state;

		private String latitude;

		private String longitude;

		private String country;

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getLongitude() {
			return longitude;
		}

		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}

		public String getLatitude() {
			return latitude;
		}

		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}

	}

	public static class ResponseDto {

		private LocationDto location;

		private String sunrise;

		private String sunset;

		public LocationDto getLocation() {
			return this.location;
		}

		public void setLocation(LocationDto location) {
			this.location = location;
		}

		public String getSunrise() {
			return sunrise;
		}

		public void setSunrise(String sunrise) {
			this.sunrise = sunrise;
		}

		public String getSunset() {
			return sunset;
		}

		public void setSunset(String sunset) {
			this.sunset = sunset;
		}

	}

}
